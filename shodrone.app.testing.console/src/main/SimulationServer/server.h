#ifndef SERVER_H
#define SERVER_H

/**
 * @brief The main function for the server thread.
 *
 * This function sets up a TCP socket, listens for incoming client connections,
 * and creates a new thread to handle each client.
 *
 * @param arg Not used.
 * @return NULL.
 */
void *server_thread(void *arg);

#endif