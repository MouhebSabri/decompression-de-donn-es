import java.io.*;//importe les classes nécessaires pour effectuer les entrées/sorties sur des fichiers.

public class HuffmanDecompression {

    // Structure de l'arbre de Huffman
    private static class Node { // stocker la structure de l'arbre de Huffman. 
    	//Un noeud contient une valeur (qui peut être un caractère ou une fréquence), 
    	//et des liens vers les noeuds enfants gauche et droit.
        public char value;
        public Node left, right;
        public Node(char value) { this.value = value; }
    }

    public static void main(String[] args) throws IOException {
        // Étape 1+2 : lecture de l'alphabet et des fréquences et la construire
        BufferedReader reader = new BufferedReader(new FileReader("exemple_freq.txt"));
        int alphabetSize = Integer.parseInt(reader.readLine());
        Node root = null;
        for (int i = 0; i < alphabetSize; i++) {// La première ligne lit le nombre de symboles 
        	//dans l'alphabet, 
        	//puis pour chaque symbole, les lignes suivantes lisent le symbole 
        	//et sa fréquence correspondante, 
            char symbol = reader.readLine().charAt(0);
            int frequency = Integer.parseInt(reader.readLine());
            root = insert(root, symbol, frequency); //puis insèrent le symbole dans l'arbre 
            //de Huffman à l'aide de la fonction "insert".
        }
        reader.close();


        // Étape 3 : décodage du texte comprimé
        InputStream input = new FileInputStream("exemple_freq_comp.bin");
        OutputStream output = new FileOutputStream("exemple_freq_decompressed.txt"); 
        //les deux premières lignes ouvrent des flux d'entrée et de sortie 
        //pour lire le fichier compressé et écrire le fichier décompressé. 
        int bit;
        Node node = root;
        while ((bit = input.read()) != -1) { //lit chaque bit du fichier compressé. 
        	//Si le bit est 0, le noeud courant est mis à jour avec le fils gauche, 
        	//sinon le noeud courant est mis à jour avec le fils droit. 
        	//Si le noeud courant n'a pas de fils gauche ni de fils droit
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

        // Étape 4 : détermination du taux de compression
        File compressedFile = new File("exemple_freq_comp.bin");
        File originalFile = new File("exemple_freq.txt");
        double compressionRatio = 1.0 - (double) compressedFile.length() / originalFile.length();
        System.out.println("Taux de compression : " + compressionRatio);

        // Étape 5 : détermination du nombre moyen de bits de stockage d'un caractère du 
        //texte compressé
        double bitsPerCharacter = (double) compressedFile.length() * 8 / originalFile.length();
        System.out.println("Nombre moyen de bits par caractère : " + bitsPerCharacter);
    }

    // Fonction pour insérer un nœud dans l'arbre de Huffman
    private static Node insert(Node root, char value, int frequency) {
    	//insérer de nouveaux nœuds dans l'arbre de Huffman en fonction de leur fréquence. 
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
