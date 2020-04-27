package dictionary;

import java.io.*;

/**
 * CompactPrefixTree class, implements Dictionary ADT and
 * several additional methods. Can be used as a spell checker.
 * Fill in code and feel free to add additional methods as needed.
 * S19
 */
public class CompactPrefixTree implements Dictionary {

    private Node root; // the root of the tree

    /**
     * Default constructor
     */
    public CompactPrefixTree() {
    }

    /**
     * Creates a dictionary ("compact prefix tree")
     * using words from the given file.
     *
     * @param filename the name of the file with words
     */
    public CompactPrefixTree(String filename) {
        try {
            FileReader fr = new FileReader(filename);
            BufferedReader br = new BufferedReader(fr);
            String line = br.readLine();
            while (line != null) {
                String[] lineWords = line.split(" ");
                for (int i = 0; i < lineWords.length; i++) {
                    add(lineWords[i]);
                }
                line = br.readLine();
            }
        } catch (IOException e) {
            System.out.println("IO Exception");
        }


    }

    /**
     * Adds a given word to the dictionary.
     *
     * @param word the word to add to the dictionary
     */
    public void add(String word) {
        root = add(word.toLowerCase(), root); // Calling private add method

    }

    /**
     * Checks if a given word is in the dictionary
     *
     * @param word the word to check
     * @return true if the word is in the dictionary, false otherwise
     */
    public boolean check(String word) {

        return check(word.toLowerCase(), root); // Calling private check method
    }

    /**
     * Checks if a given prefix is stored in the dictionary
     *
     * @param prefix The prefix of a word
     * @return true if this prefix is a prefix of any word in the dictionary,
     * and false otherwise
     */
    public boolean checkPrefix(String prefix) {
        return checkPrefix(prefix.toLowerCase(), root); // Calling private checkPrefix method
    }

    /**
     * Returns a human-readable string representation of the compact prefix tree;
     * contains nodes listed using pre-order traversal and uses indentations to show the level of the node.
     * An asterisk after the node means the node's boolean flag is set to true.
     * The root is at the current indentation level (followed by * if the node's valid bit is set to true),
     * then there are children of the node at a higher indentation level.
     */
    public String toString() {
        String s = toString(root, 0);

        return s;
    }

    /**
     * Print out the nodes of the tree to a file, using indentations to specify the level
     * of the node.
     *
     * @param filename the name of the file where to output the tree
     */
    public void printTree(String filename) {
        try {
            File file = new File("DictionaryWords.txt");
            FileWriter fw = new FileWriter(file);
            BufferedWriter bw = new BufferedWriter(fw);


        } catch (IOException e) {
            System.out.println("IO exception.");
        }


    }

    /**
     * Return an array of the entries in the dictionary that are as close as possible to
     * the parameter word.  If the word passed in is in the dictionary, then
     * return an array of length 1 that contains only that word.  If the word is
     * not in the dictionary, then return an array of numSuggestions different words
     * that are in the dictionary, that are as close as possible to the target word.
     * Implementation details are up to you, but you are required to make it efficient
     * and make good use ot the compact prefix tree.
     *
     * @param word           The word to check
     * @param numSuggestions The length of the array to return.  Note that if the word is
     *                       in the dictionary, this parameter will be ignored, and the array will contain a
     *                       single world.
     * @return An array of the closest entries in the dictionary to the target word
     */

    public String[] suggest(String word, int numSuggestions) {
        if (check(word) == true) {
            String[] trueWord = {word};
            return trueWord;
        } else {
            String[] suggestions = new String[numSuggestions];
            for (int i = 0; i < numSuggestions; i++) {

            }
        }


        return null; // don't forget to change it
    }

    // ---------- Private helper methods ---------------

    /**
     * A private add method that adds a given string to the tree
     *
     * @param s    the string to add
     * @param node the root of a tree where we want to add a new string
     * @return a reference to the root of the tree that contains s
     */
    private Node add(String s, Node node) {
        Node newNode = new Node();

        int searchIndex = Character.toLowerCase(s.charAt(0)) - 97; //97 is the ASCII value for lowercase 'a'
        if (node == null) {
            node = new Node();
            node.prefix = "";
            newNode.prefix = s;
            node.children[searchIndex] = newNode;
            newNode.isWord = true;
        } else {
            Node searchNode = node.children[searchIndex];

            if (searchNode == null) {
                newNode.prefix = s;
                node.children[searchIndex] = newNode;
                newNode.isWord = true;
            } else {
                if (searchNode.prefix.equals(s)) {
                    searchNode.isWord = true;
                } else if (comparePrefix(searchNode.prefix, s) > 0) {
                    int preIndex = comparePrefix(searchNode.prefix, s); //index of first letter not contained in the prefix
                    String ogPre = searchNode.prefix;
                    String suffix = s.substring(preIndex); //suffix is suffix of word being entered
                    if (preIndex == ogPre.length()) { //just adds new suffix to common prefix
                        add(suffix, searchNode);
                    } else { //if original prefix needs to be split up
                        String suffixWord = ogPre.substring(preIndex); //suffix word is suffix of word already stored in node
                        newNode.prefix = ogPre.substring(0, preIndex); //makes the common prefix the prefix of new node
                        searchNode.prefix = suffixWord; // sets the suffix of the original node to be the prefix of the original node
                        //searchNode.isWord = true;
                        newNode.children[Character.toLowerCase(searchNode.prefix.charAt(0)) - 97] = searchNode; // sets the original node as the child of the new node with the common prefix
                        add(suffix, newNode); //recursively calls add method with new suffix on the subtree created by the new node
                        node.children[searchIndex] = newNode;
                    }
                }
            }
        }


        return node; // don't forget to change it
    }


    /**
     * A private method to check whether a given string is stored in the tree.
     *
     * @param s    the string to check
     * @param node the root of a tree
     * @return true if the prefix is in the dictionary, false otherwise
     */
    private boolean check(String s, Node node) {
        int searchIndex = Character.toLowerCase(s.charAt(0)) - 97;
        Node searchNode = node.children[searchIndex];


        if (node == null || searchNode == null) {
            return false;
        } else if (!s.toLowerCase().contains(searchNode.prefix)) {
            return false;
        } else if (searchNode.prefix.equals(s)) {
            if (searchNode.isWord) {
                return true;
            } else {
                return false;
            }
        } else {
            int sufIndex = comparePrefix(searchNode.prefix, s);
            String suffix = s.substring(sufIndex);
            check(suffix, searchNode);
        }

        return true;
    }

    /**
     * A private recursive method to check whether a given prefix is in the tree
     *
     * @param prefix the prefix
     * @param node   the root of the tree
     * @return true if the prefix is in the dictionary, false otherwise
     */
    private boolean checkPrefix(String prefix, Node node) {
        int searchIndex = Character.toLowerCase(prefix.charAt(0)) - 97;
        Node searchNode = node.children[searchIndex];

        if (node == null || searchNode == null) {
            return false;
        }

        int comparison = comparePrefix(searchNode.prefix, prefix);

        if (searchNode.prefix.equalsIgnoreCase(prefix)) {
            return true;
        } else {
            if (searchNode.prefix.length() < prefix.length()) {
                if (comparison == searchNode.prefix.length()) { //runs if the entire node prefix is part of given prefix
                    prefix = prefix.substring(comparison); //trims prefix to remove what's already been confirmed
                    return checkPrefix(prefix, searchNode);
                }
                return false;
            } else if (searchNode.prefix.length() >= prefix.length()) { //checks whether the prefix is part of the word
                if (comparison == prefix.length()) {
                    return true;
                }
                return false;
            }
        }

        return true;
    }

    /**************HELPER METHODS ******************/

    public String closestPrefix(Node node, String word) {
        if(node == null){

        }

        return word;
    }

    public int comparePrefix(String prefix, String s) { //returns index of last char in s and prefix where they are equal.

        int i = 0;
        while (i < prefix.length() && i < s.length() && Character.toLowerCase(prefix.charAt(i)) == Character.toLowerCase(s.charAt(i))) {
            i++;
        }
        return i;
    }

    /*code testing methods*/
    public void printPreOrder() {
        printPreOrder(root);
    }

    private void printPreOrder(Node node) {
        if (node == null) {
            return;
        }
        if(node.isWord){
            System.out.println(node.prefix + "*");
        }else {
            System.out.println(node.prefix);
        }
        for (int i = 0; i < 26; i++) {
            printPreOrder(node.children[i]);
        }
    }
    private String toString(Node node, int numIndentations){
        String s = "";
        if(node == null){
            return s;
        }
        for (int i = 0; i < numIndentations; i++){
            s += " ";
        }
        if (node.isWord){
            s +=node.prefix + "*" +  "\n";
        }else {
            s += node.prefix +  "\n";
        }
        numIndentations += 1;
        for(int i = 0; i < 26; i++){
            s += toString(node.children[i], numIndentations);
        }
        return s;
    }

    // You might want to create a private recursive helper method for toString
    // that takes the node and the number of indentations, and returns the tree  (printed with indentations) in a string.
    // private String toString(Node node, int numIndentations)


    // Add a private suggest method. Decide which parameters it should have

    // --------- Private class Node ------------
    // Represents a node in a compact prefix tree
    private class Node {
        String prefix; // prefix stored in the node
        Node children[]; // array of children (26 children)
        boolean isWord; // true if by concatenating all prefixes on the path from the root to this node, we get a valid word

        Node() {
            isWord = false;
            prefix = "";
            children = new Node[26]; // initialize the array of children
        }

        // FILL IN CODE: Add other methods to class Node as needed
    }

}
