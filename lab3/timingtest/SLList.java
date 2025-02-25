package timingtest;

/** An SLList is a list of integers, which hides the terrible truth
 * of the nakedness within. */
public class SLList<Item> {
	private class IntNode {
		public Item item;
		public IntNode next;
		public IntNode(Item i, IntNode n) {
			item = i;
			next = n;
		}
	}
	private IntNode last_node;

	/* The first item (if it exists) is at sentinel.next. */
	private IntNode sentinel;
	private int size;

	/** Creates an empty timingtest.SLList. */
	public SLList() {
		sentinel = new IntNode(null, null);
		this.last_node = sentinel;
		size = 0;
	}

	public SLList(Item x) {
		sentinel = new IntNode(null, null);
		sentinel.next = new IntNode(x, null);
		this.last_node = sentinel.next;
		size = 1;
	}

	/** Adds x to the front of the list. */
	public void addFirst(Item x) {
		sentinel.next = new IntNode(x, sentinel.next);
		size = size + 1;
	}

	/** Returns the first item in the list. */
	public Item getFirst() {
		return sentinel.next.item;
	}

	/** Adds x to the end of the list. */
	public void addLast(Item x) {
		size = size + 1;

//		IntNode p = sentinel;
//
//		/* Advance p to the end of the list. */
//		while (p.next != null) {
//			p = p.next;
//		}
		last_node.next = new IntNode(x, null);
		last_node = last_node.next;
	}

	/** returns last item in the list */
	public Item getLast() {
//		IntNode p = sentinel;
//
//		/* Advance p to the end of the list. */
//		while (p.next != null) {
//			p = p.next;
//		}
		if (last_node == sentinel) {
			return null;
		}else{
			return last_node.item;
		}
	}


	/** Returns the size of the list. */
	public int size() {
		return size;
	}

	public static void main(String[] args) {
		/* Creates a list of one integer, namely 10 */
		SLList L = new SLList();
		L.addLast(20);
		System.out.println(L.size());
	}
}
