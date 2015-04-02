
import java.util.ArrayList;

import prange.Cribbage.Hand;
import prange.Cribbage.Card;
import prange.Cribbage.Card.Suit;
import prange.Cribbage.Card.CardValue;

public class Runner {

	public static void main(String[] args) {
		
		long startTime = System.nanoTime();
		
		//calculateNonPointsFiveHands(true);
		calculateAllNonPointsHands(true);
		
		long finishTime = System.nanoTime();
		long timeTaken = finishTime - startTime;
		System.out.println("Calculation Time: " + timeTaken + "ns");
		double time = (double)timeTaken;
		System.out.println("Calculation Time: " + Math.round(time / Math.pow(10, 6)) + "ms");
		System.out.println("Calculation Time: " + Math.round(time / Math.pow(10, 9)) + "s");
	}
	
	public static void calculateAllNonPointsHands(boolean isCrib)
	{
		int count = 0;
		ArrayList<CardValue> keepValues = new ArrayList<CardValue>();
		for (CardValue first : CardValue.values())
		{
			for (CardValue second : CardValue.values())
			{
				keepValues.add(first);
				keepValues.add(second);
				count += runLoop(first, second, genAllCards(keepValues), isCrib);
				keepValues.clear();
			}
		}
		int totalHands = 52 * 51 * 50 * 49 * 48;
		System.out.println("Total non-point hands: " + count);
		System.out.println("Total number of hands: " + totalHands);
		System.out.println("Statistical percentage of non-points hands: " + (double)count / totalHands * 100);
	}
	
	public static void calculateNonPointsFiveHands(boolean isCrib)
	{
		CardValue firstValue = CardValue.Two;
		CardValue secondValue = CardValue.Three;
		ArrayList<CardValue> keepValues = new ArrayList<CardValue>();
		keepValues.add(firstValue);
		keepValues.add(secondValue);
		int count = runLoop(firstValue, secondValue, genAllCards(keepValues), isCrib);
		
		firstValue = CardValue.Four;
		secondValue = CardValue.Ace;
		keepValues.clear();
		keepValues.add(firstValue);
		keepValues.add(secondValue);
		count += runLoop(firstValue, secondValue, genAllCards(keepValues), isCrib);
		
		firstValue = CardValue.Five;
		for (CardValue second : CardValue.values())
		{
			keepValues.clear();
			keepValues.add(firstValue);
			keepValues.add(second);
			count += runLoop(firstValue, second, genAllCards(keepValues), isCrib);
		}
		
		System.out.println(count);
		
	}
	
	public static ArrayList<Card> genAllCards(ArrayList<CardValue> removeCardVals)
	{
		ArrayList<Card> returnVal = new ArrayList<Card>();
		for (CardValue val:CardValue.values())
		{
			if (!removeCardVals.contains(val))
			{
				for (Suit suit:Suit.values())
				{
					returnVal.add(new Card(val, suit));	
				}
			}
		}
		return returnVal;
	}
	
	public static int runLoop(CardValue first, CardValue second, ArrayList<Card> remainingCards, boolean isCrib)
	{
		int count = 0;
		for (Suit firstSuit : Suit.values())
		{
			for (Suit secondSuit : Suit.values())
			{
				Card firstCard = new Card(first, firstSuit);
				Card secondCard = new Card(second, secondSuit);
				
				for (int i = 0 ; i < remainingCards.size(); i++)
				{
					for (int k = i+1 ; k < remainingCards.size(); k++)
					{
						for (int r = k+1 ; r < remainingCards.size() ; r++)
						{
							Hand newHand = new Hand(remainingCards.get(i), remainingCards.get(k),
													remainingCards.get(r), firstCard, secondCard, isCrib);
							if (!newHand.hasPoints())
							{
								//System.out.println(newHand);
								count++;
							}
						}
					}
				}
			}
		}
		return count;
	}

}
