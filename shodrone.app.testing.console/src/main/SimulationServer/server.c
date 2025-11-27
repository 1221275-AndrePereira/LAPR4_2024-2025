#include "simulation.h"
#include "server.h"
#include <sys/socket.h> // Required for socket functions (socket, bind, listen, etc.)
#include <netinet/in.h> // Required for sockaddr_in, INADDR_ANY, htons

#define PORT 8080
#define MAX_CLIENTS 5

void *handle_client(void *socket_desc) {
    int sock = *(int*)socket_desc;
    free(socket_desc);
    char buffer[2048];
    int read_size;

    // Loop to handle multiple requests from the same client
    while ((read_size = recv(sock, buffer, sizeof(buffer) - 1, 0)) > 0) {
        buffer[read_size] = '\0';
        char response[256];

        char *saveptr; // For strtok_r
        char *command = strtok_r(buffer, ";\n", &saveptr);

        if (command && strcmp(command, "SIMULATE") == 0) {
            pthread_mutex_lock(&shared_mem_mutex);

            if (shared_mem->sim_state == STATE_IDLE) {
                // Prepare for a new simulation run. This resets drone counts, status, etc.
                initialize_simulation();

                shared_mem->drone_count = 0;
                char *filename;
                while ((filename = strtok_r(NULL, ";\n", &saveptr)) != NULL && shared_mem->drone_count < MAX_DRONES) {
                    if (strlen(filename) > 0) {
                        strncpy(shared_mem->drone_files[shared_mem->drone_count], filename, MAX_FILENAME_LEN - 1);
                        shared_mem->drone_files[shared_mem->drone_count][MAX_FILENAME_LEN - 1] = '\0';
                        shared_mem->drone_count++;
                    }
                }

                // Set state to start the simulation and signal the main loop
                shared_mem->sim_state = STATE_SIMULATING;
                pthread_cond_signal(&state_change_cond);

                // Wait until the simulation is complete
                while (shared_mem->sim_state != STATE_DONE) {
                    pthread_cond_wait(&client_cond, &shared_mem_mutex);
                }

                // The simulation is finished. Prepare the final response.
                if (shared_mem->status == SIM_VALID) {
                    snprintf(response, sizeof(response), "SUCCESS\n");
                } else {
                    snprintf(response, sizeof(response), "FAILURE\n");
                }

                // Reset the system to IDLE for the next request
                shared_mem->sim_state = STATE_IDLE;
            } else {
                snprintf(response, sizeof(response), "FAILURE; System is busy.\n");
            }
            pthread_mutex_unlock(&shared_mem_mutex);
        } else {
            snprintf(response, sizeof(response), "FAILURE; Unknown or invalid command.\n");
        }

        send(sock, response, strlen(response), 0);
        // Send an extra newline to terminate the client's read loop gracefully
        send(sock, "\n", 1, 0);
    }

    if (read_size == 0) {
        printf("Client disconnected.\n");
    } else if (read_size == -1) {
        perror("recv failed");
    }

    close(sock);
    return NULL;
}

void *server_thread(void *arg) {
    int server_fd, new_socket;
    struct sockaddr_in address;
    socklen_t addrlen = sizeof(address);

    server_fd = socket(AF_INET, SOCK_STREAM, 0);
    if (server_fd == -1) {
        perror("socket failed");
        exit(EXIT_FAILURE);
    }

    int opt = 1;
    setsockopt(server_fd, SOL_SOCKET, SO_REUSEADDR, &opt, sizeof(opt));

    address.sin_family = AF_INET;
    address.sin_addr.s_addr = INADDR_ANY;
    address.sin_port = htons(PORT);

    if (bind(server_fd, (struct sockaddr *)&address, sizeof(address)) < 0) {
        perror("bind failed");
        exit(EXIT_FAILURE);
    }
    if (listen(server_fd, MAX_CLIENTS) < 0) {
        perror("listen failed");
        exit(EXIT_FAILURE);
    }


    printf("Server listening on port %d. Waiting for commands...\n", PORT);

    while (1) {
        new_socket = accept(server_fd, (struct sockaddr *)&address, &addrlen);
        if (new_socket < 0) {
            perror("accept failed");
            continue;
        }

        pthread_t client_thread;
        int *new_sock = malloc(sizeof(int));
        *new_sock = new_socket;
        if (pthread_create(&client_thread, NULL, handle_client, (void*) new_sock) != 0) {
            perror("could not create client thread");
            free(new_sock);
        } else {
            pthread_detach(client_thread);
        }
    }
    close(server_fd);
    return NULL;
}
