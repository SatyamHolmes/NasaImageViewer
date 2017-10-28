class Store<E>
{
	private StoreNode<E> head;
	private int num;
	
	public Store()
	{
		head=new StoreNode<E>(null);
		head.next=head;
		head.prev=head;
		num=0;
	}
	
	public void addFront(E element)
	{
		StoreNode temp=new StoreNode<E>(element);
		temp.next=head.next;
		temp.prev=head;
		head.next=temp;
		temp.next.prev=temp;
		num++;
	}

	public void addLast(E element)
	{
		StoreNode temp=new StoreNode<E>(element);
		temp.next=head;
		temp.prev=head.prev;
		head.prev=temp;	
		temp.prev.next=temp;
		num++;
	}

	public void removeFront()
	{
		StoreNode temp=head.next;
		head.next=temp.next;
		temp.next.prev=head;
		num--;
	}

	public void removeLast()
	{
		StoreNode temp=head.prev;
		head.prev=temp.prev;
		temp.prev.next=head;
		num--;
	}

	public E get(int i)
	{
		if(head.next!=head)
		{
			StoreNode<E> temp=head;
			for(int j=0;j<i;j++)
				temp=temp.prev;
			return temp.elem;
		}
		else
			return null;
	}

	public int size()
	{
		return num;
	}

	class StoreNode<E>
	{
		public E elem;
		public StoreNode<E> next;
		public StoreNode<E> prev;

		public StoreNode(E element)
		{
			elem=element;
			next=null;
			prev=null;
		}
	}
}