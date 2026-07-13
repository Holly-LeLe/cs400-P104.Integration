public class RedBlackTree<T extends Comparable<T>> extends BinarySearchTree<T> {

  @Override
  public void insert(T data) throws NullPointerException {
    if (data == null)
      throw new NullPointerException("data cannot be null");

    RedBlackNode<T> newNode = new RedBlackNode<>(data);

    if (this.root == null) {
      this.root = newNode;
    } else {
      this.insertHelper(newNode, this.root);
      this.ensureRedProperty(newNode);
    }

    ((RedBlackNode<T>) this.root).isBlackNode = true;
  }

  protected void ensureRedProperty(RedBlackNode<T> newNode) {
    RedBlackNode<T> parent = newNode.getUp();

    if (parent == null || parent.isBlackNode())
      return;

    RedBlackNode<T> grandparent = parent.getUp();
    if (grandparent == null)
      return;

    RedBlackNode<T> aunt;
    if (parent == grandparent.getLeft())
      aunt = grandparent.getRight();
    else
      aunt = grandparent.getLeft();

    if (aunt != null && !aunt.isBlackNode()) {
      parent.isBlackNode = true;
      aunt.isBlackNode = true;
      grandparent.isBlackNode = false;
      ensureRedProperty(grandparent);
      return;
    }

    if (parent == grandparent.getLeft() && newNode == parent.getRight()) {
      rotate(newNode, parent);
      newNode = parent;
      parent = newNode.getUp();
    } else if (parent == grandparent.getRight() && newNode == parent.getLeft()) {
      rotate(newNode, parent);
      newNode = parent;
      parent = newNode.getUp();
    }

    grandparent = parent.getUp();

    parent.isBlackNode = true;
    grandparent.isBlackNode = false;
    rotate(parent, grandparent);
  }
}
