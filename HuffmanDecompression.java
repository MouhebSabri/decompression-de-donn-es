import java.io.*;

public class HuffmanDecompression {

    // Structure de l'arbre de Huffman
    private static class Node {
        public char value;
        public Node left, right;
        public Node(char value) { this.value = value; }
    }

    public static void main(String[] args) throws IOException {
        // �tape 1 : lecture de l'alphabet et des fr�quences
        BufferedReader reader = new BufferedReader(new FileReader("alphabet.txt"));
        int alphabetSize = Integer.parseInt(reader.readLine());
        Node root = null;
        for (int i = 0; i < alphabetSize; i++) {
            char symbol = reader.readLine().charAt(0);
            int frequency = Integer.parseInt(reader.readLine());
            root = insert(root, symbol, frequency);
        }
        reader.close();

        // �tape 2 : construction de l'arbre de Huffman
        // L'arbre a d�j� �t� construit � l'�tape 1

        // �tape 3 : d�codage du texte comprim�
        InputStream input = new FileInputStream("compressed.bin");
        OutputStream output = new FileOutputStream("decompressed.txt");
        int bit;
        Node node = root;
        while ((bit = input.read()) != -1) {
            if (bit == '0') {
                node = node.left;
            } else if (bit == '1') {
                node = node.right;
            }
            if (node.left == null && node.right == null) {
                output.write(node.value);
                node = root;
            }
        }
        input.close();
        output.close();

        // �tape 4 : d�termination du taux de compression
        File compressedFile = new File("compressed.bin");
        File originalFile = new File("original.txt");
        double compressionRatio = 1.0 - (double) compressedFile.length() / originalFile.length();
        System.out.println("Taux de compression : " + compressionRatio);

        // �tape 5 : d�termination du nombre moyen de bits de stockage d'un caract�re du texte compress�
        double bitsPerCharacter = (double) compressedFile.length() * 8 / originalFile.length();
        System.out.println("Nombre moyen de bits par caract�re : " + bitsPerCharacter);
    }

    // Fonction pour ins�rer un n�ud dans l'arbre de Huffman
    private static Node insert(Node root, char value, int frequency) {
        if (root == null) {
            return new Node(value);
        } else if (frequency < root.value) {
            Node node = new Node(value);
            node.left = root;
            return node;
        } else {
            root.right = insert(root.right, value, frequency);
            return root;
        }
    }
}
