package prange.Cribbage;

public class Card
{
	
	private int m_value;
	private Suit m_suit;
	private CardValue m_card;

	public Card(CardValue num, Suit suit)
	{
		m_card = num;
		m_suit = suit;
		m_value = num.ordinal()+1;
		if (m_value > 10)
		{
			m_value = 10;
		}
	}
	
	public int getValue()
	{
		return m_value;
	}
	
	public Suit getSuit()
	{
		return m_suit;
	}
	
	public CardValue getCard()
	{
		return m_card;
	}

	public boolean isSuit(Suit suit)
	{
		return m_suit == suit;
	}
	
	public boolean isValue(int value)
	{
		return m_value == value;
	}
	
	public boolean isCard(CardValue card)
	{
		return m_card == card;
	}
	
	public String toString()
	{
		return m_card + ":" + m_suit;
	}
	
	public enum CardValue 
	{
		Ace,Two,Three,Four,Five,Six,Seven,Eight,Nine,Ten,Jack,Queen,King;
		private static CardValue[] vals = values();
		public CardValue getNext()
		{
			if (this.ordinal() == vals.length - 1)
				return null;
			
			return vals[(this.ordinal() + 1) % vals.length];
		}
	}
	
	public enum Suit
	{
		Club,Spade,Heart,Diamond
	}
}
