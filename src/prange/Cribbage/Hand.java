package prange.Cribbage;

import prange.Cribbage.Card;
import prange.Cribbage.Card.CardValue;

public class Hand {

	private Card[] m_hand;
	private Card m_cutCard;
	private Card[] m_fullHand;
	private int[] m_values;
	private boolean m_isCrib;
	
	public Hand(Card cut, Card first, Card second, Card third, Card fourth, boolean crib)
	{
		m_cutCard = cut;
		m_hand = new Card[] {first, second, third, fourth};
		m_fullHand = new Card[] {cut, first, second, third, fourth};
		m_isCrib = crib;
	}
	
	Hand(Card cut, Card[] hand, boolean crib)
	{
		m_cutCard = cut;
		this.m_hand = hand;
		m_fullHand = new Card[] {cut, hand[0], hand[1], hand[2], hand[3], hand[4]};
		m_isCrib = crib;
	}
	
	public boolean hasPoints() 
	{
		/** 
		 * This method simply shortcuts whether a hand has points or not.
		 * Points can be scored in the following ways
		 * Fifteen: Each set of cards that adds to 15 is worth two points
		 * Pairs: Each pair of cards is worth two points
		 * Runs: Each card in a run over three cards is worth the number of cards in the run
		 * Flush: If hand is a crib, the all five cards need to be of the same suit to score five points
		 * 	If hand is not a crib, the four cards in the hand need to be the same suit to score four points, with the optional cut
		 * Nobs: If you have a Jack that is the same suit as the cut, you get a point
		 **/
		
		//Nobs Check as first double loop, also Flush
		boolean isFlush = true;
		Card.Suit initSuit = m_hand[0].getSuit();
		for (Card card : m_hand)
		{
			if (card.isCard(CardValue.Jack) && m_cutCard.isSuit(card.getSuit()))
			{
				return true;
			}
			if (!card.getSuit().equals(initSuit))
			{
				isFlush = false;
			}
		}
		if (isFlush)
		{
			return true;
		}
		
		getIntArray();
		
		//Runs Check
		int maxRuns = 0;
		int tempRuns = 0;
		Card lastCard = null;
		for (Card card : sort(m_fullHand))
		{
			if (lastCard != null && lastCard.getCard().getNext() == null) {break;}
			
			if (lastCard == null  || lastCard.getCard().getNext().equals(card.getCard()))
			{
				tempRuns++;
			}
			else
			{
				tempRuns = 1;
			}
			
			if (tempRuns > maxRuns)
			{
				maxRuns = tempRuns;
				
			}
			lastCard = card;
		}
		if (maxRuns >= 3)
		{
			return true;
		}
		//Double loop for pairs
		for (int i = 0 ; i < m_hand.length ; i++)
		{
			for (int k = i+1 ; k < 5 ; k++)
			{
				if (m_fullHand[i].isCard(m_fullHand[k].getCard()))
				{
					return true;
				}
			}
		}
		
		if (anySums(15))
		{
			return true;
		}
		
		
		
		return false;
	}
	
	public int numPoints()
	{
		/** 
		 * This method simply shortcuts whether a hand has points or not.
		 * Points can be scored in the following ways
		 * Fifteen: Each set of cards that adds to 15 is worth two points
		 * Pairs: Each pair of cards is worth two points
		 * Runs: Each card in a run over three cards is worth the number of cards in the run
		 * Flush: If hand is a crib, the all five cards need to be of the same suit to score five points
		 * 	If hand is not a crib, the four cards in the hand need to be the same suit to score four points, with the optional cut
		 * Nobs: If you have a Jack that is the same suit as the cut, you get a point
		 **/
		
		int points = 0;
		
		//Nobs Check as first double loop, also Flush
		boolean isFlush = true;
		Card.Suit initSuit = m_hand[0].getSuit();
		for (Card card : m_hand)
		{
			if (card.isCard(CardValue.Jack) && m_cutCard.isSuit(card.getSuit()))
			{
				points += 1;
			}
			if (!card.getSuit().equals(initSuit))
			{
				isFlush = false;
			}
		}
		if (isFlush)
		{
			if (m_isCrib)
			{
				points += m_cutCard.isSuit(initSuit) ? 5 : 0;
			}
			else
			{
				points += 4;
			}
		}
		
		getIntArray();
		
		//Runs Check
		int maxRuns = 0;
		int tempRuns = 0;
		Card lastCard = null;
		for (Card card : sort(m_fullHand))
		{
			if (lastCard == null || lastCard.getCard().getNext().equals(card.getCard()))
			{
				tempRuns++;
			}
			else if (tempRuns > maxRuns)
			{
				maxRuns = tempRuns;
				tempRuns = 0;
			}
		}
		if (maxRuns >= 3)
		{
			points += 3;
		}
		//Double loop for pairs
		for (int i = 0 ; i < m_hand.length ; i++)
		{
			for (int k = i+1 ; k < 5 ; k++)
			{
				if (m_fullHand[i].isCard(m_fullHand[k].getCard()))
				{
					points += 2;
				}
			}
		}
		
		points += numSums(15) * 2;
		
		return points;
	}
	
	private void getIntArray()
	{
		m_values = new int[m_fullHand.length];
		for (int i = 0 ; i < m_fullHand.length ; i++)
		{
			m_values[i] = m_fullHand[i].getValue();
		}
	}
	
	private Card[] sort(Card[] values)
	{
		
		Card[] returnVal = new Card[values.length];
		
		for (int i = 0 ; i < values.length ; i++)
		{
			int state = 0;
			Card currentValue = values[i];
			for (int k = 0 ; k < returnVal.length ; k++)
			{
				switch(state) 
				{
					case 0://Initial searching for higher value state
						if (returnVal[k] == null)
						{
							returnVal[k] = currentValue;
							state = 2;
						}
						else if (returnVal[k].getCard().ordinal() > currentValue.getCard().ordinal())
						{
							Card temp = returnVal[k];
							returnVal[k] = currentValue;
							currentValue = temp;
							state = 1;
						}
						break;
					case 1://swapping state
						if (returnVal[k] == null)
						{
							returnVal[k] = currentValue;
							state = 2;
						}
						else
						{
							Card temp = returnVal[k];
							returnVal[k] = currentValue;
							currentValue = temp;
						}
						break;
					default://reached end of allocated values state
						continue;
				}
			}
		}
				
		return returnVal;
	}
	
	public boolean anySums(int target)
	{
		
		return anySums(target, 0, m_values);
	}
	
	public static boolean anySums(int target, int startNum, int[] values)
	{
		if (startNum == target)
		{
			return true;
		}
		else if (startNum > target)
		{
			return false;
		}
		else
		{
			for (int i = 0 ; i < values.length ; i++)
			{
				if (values[i] != 0)
				{
					int callVal = values[i];
					values[i] = 0;
					if (anySums(target, startNum+callVal,values.clone()))
					{
						return true;
					}
				}
			}
		}
		return false;
	}
	
	public int numSums(int target)
	{
		return numSums(target,0,m_values);
	}
	
	public static int numSums(int target, int startNum, int[] values)
	{
		
		if (startNum == target)
		{
			return 1;
		}
		else if (startNum > target)
		{
			return 0;
		}
		else
		{
			int count = 0;
			for (int i = 0 ; i < values.length ; i++)
			{
				if (values[i] == 0)
				{
					int callVal = values[i];
					values[i] = 0;
					count += numSums(target, startNum+callVal,values.clone());
				}
			}
			return count;
		}
		
	}
	
	public Card getCutCard()
	{
		return m_cutCard;
	}

	public Card[] getHand()
	{
		return m_hand;
	}

	public Card[] getFullHand()
	{
		return m_fullHand;
	}
	
	public String toString()
	{
		String build = "";
		for (Card card : m_fullHand)
		{
			build += card.toString() + ";";
		}
		return build+"Crib="+m_isCrib;
	}

}
