package utils; // Or a more general util package

import eapli.framework.io.util.Console; // Assuming you use EAPLI's Console

import java.util.List;
import java.util.Optional;
import java.util.function.Function;

public class ListPrompt<T> {

    private final String title;
    private final List<T> items;
    private final Function<T, String> itemRenderer;

    public ListPrompt(String title, List<T> items) {
        this(title, items, T::toString);
    }

    public ListPrompt(String title, List<T> items, Function<T, String> itemRenderer) {
        if (title == null || title.trim().isEmpty()) {
            throw new IllegalArgumentException("Title cannot be null or empty.");
        }
        if (items == null) {
            throw new IllegalArgumentException("Item list cannot be null.");
        }
        if (itemRenderer == null) {
            throw new IllegalArgumentException("Item renderer cannot be null.");
        }
        this.title = title;
        this.items = items;
        this.itemRenderer = itemRenderer;
    }

    public Optional<T> selectItem() {
        if (items.isEmpty()) {
            return Optional.empty();
        }

        System.out.println("\n--- " + title + " ---");
        for (int i = 0; i < items.size(); i++) {
            // Use the itemRenderer to get the display string for the item
            System.out.printf("%d. %s%n", i + 1, itemRenderer.apply(items.get(i)));
        }
        System.out.println("0. Cancel");
        System.out.println("------------------------------------");

        int choice = -1;
        while (true) {
            try {
                choice = Console.readInteger("Enter your choice (number):");
                if (choice >= 0 && choice <= items.size()) {
                    break;
                } else {
                    System.out.println("Invalid choice. Please enter a number between 0 and " + items.size() + ".");
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a number.");
            }
        }

        if (choice == 0) {
            return Optional.empty();
        } else {
            return Optional.ofNullable(items.get(choice - 1)); // Adjust for 0-based list index
        }
    }


    public T selectItemRequired() {
        if (items.isEmpty()) {
            throw new IllegalStateException("Cannot select from an empty list of items for: " + title);
        }

        System.out.println("\n--- " + title + " (Selection Required) ---");
        for (int i = 0; i < items.size(); i++) {
            System.out.printf("%d. %s%n", i + 1, itemRenderer.apply(items.get(i)));
        }
        System.out.println("------------------------------------");

        int choice = -1;
        while (true) {
            try {
                choice = Console.readInteger("Enter your choice (number):");
                if (choice >= 1 && choice <= items.size()) {
                    break; // Valid choice for an item
                } else {
                    System.out.println("Invalid choice. Please enter a number between 1 and " + items.size() + ".");
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a number.");
            }
        }
        return items.get(choice - 1); // Adjust for 0-based list index
    }
}
