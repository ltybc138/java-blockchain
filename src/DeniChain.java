public class DeniChain {
    public static void main(String[] args) {
        Block firstBlock = new Block("First block", "0");
        System.out.println("Hash for block 1 is : " + firstBlock.hash);


        Block secondBlock = new Block("Second block", firstBlock.hash);
        System.out.println("Hash for block 2 is : " + secondBlock.hash);


        Block thirdBlock = new Block("Third block", secondBlock.hash);
        System.out.println("Hash for block 3 is : " + thirdBlock.hash);
    }
}
