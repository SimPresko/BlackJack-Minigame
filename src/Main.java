import java.util.*;

public class Main {
    public static void main(String[] args) {
        String name = nameEntrance();
        contestantWelcome(name);
        int bet = declareBetAmount(200);
        String[] cards = new String[]{"2","3","4","5","6","7","8","9","10","J","Q","K","A"};
        startNewGame(name,bet,200-bet, cards);
    }

    //Save the name of the contestant
    public static String nameEntrance(){
        System.out.print("Please Enter your name, dear contestant: ");
        Scanner scanner = new Scanner(System.in);
        String name = scanner.nextLine();
        if (name.isEmpty()) {
            name = nameEntrance();
        }
        return name;
    }

    //Welcome the contestant
    public static void contestantWelcome(String name){
        System.out.println("Welcome " + name + " to our BlackJack Casino");
        System.out.println("I am your Dealer, Mr. Zero");
        System.out.println("Here is small gift from the casino");
        System.out.println("+200$ added to your balance");
        System.out.print("Before I deal your first hand, choose how much money you want to bet: ");
    }


    //Begin a new game
    public static void startNewGame(String name, int bet, int balance, String[] cardDeck){
        System.out.println();
        System.out.println("Playing a game for " + bet + "$");
        String playerFirstCard = cardDeck[new Random().nextInt(cardDeck.length)];
        String playerSecondCard = cardDeck[new Random().nextInt(cardDeck.length)];
        String dealerFirstCard = cardDeck[new Random().nextInt(cardDeck.length)];
        String dealerSecondCard = cardDeck[new Random().nextInt(cardDeck.length)];
        List<String> playerCards = new ArrayList<>();
        playerCards.add(playerFirstCard);
        playerCards.add(playerSecondCard);
        List<String> dealerCards = new ArrayList<>();
        dealerCards.add(dealerFirstCard);
        dealerCards.add(dealerSecondCard);
        int newBalance = chooseOption(name,bet,balance,cardDeck,playerCards,dealerCards);
        boolean answer = askForNextGame(name, newBalance);
        if (answer) {
            System.out.print("Choose how much money do you want to bet next: ");
            int newBet = declareBetAmount(newBalance);
            startNewGame(name,newBet,newBalance-newBet,cardDeck);
        } else{
            endGame(name, newBalance);
        }
    }


    //Choose an Option
    public static int chooseOption(String name, int bet, int balance, String[] cardDeck, List<String> pCards, List<String> dCards){
        System.out.println();
        System.out.print("Your Cards: ");
        for (int i = 0;i<pCards.size();i++){
            if (i == pCards.size()-1){
                System.out.print(pCards.get(i));
                break;
            }
            System.out.print(pCards.get(i)+", ");
        }
        System.out.println();
        System.out.print("Dealer's Cards: ");
        System.out.print( dCards.getFirst());
        System.out.print( ", ?");
        System.out.println();
        System.out.println();
        int blackjackPlayer = checkIfBlackjack(pCards);
        int blackjackDealer = checkIfBlackjack(dCards);
        if (blackjackPlayer==2&&blackjackDealer==2) return tieMessage(name,bet,balance);
        else if (blackjackPlayer==1){
            System.out.println("Type 'h' if you want to hit and 's' if you want to stand");
            System.out.print("What do you pick (h,s): ");
            Scanner scanner = new Scanner(System.in);
            char option = scanner.next().charAt(0);
            if (option=='h'){
                pCards.add(cardDeck[new Random().nextInt(cardDeck.length)]);
                return chooseOption(name, bet, balance, cardDeck,pCards,dCards);
            } else if(option == 's'){
                int result = optionStand(cardDeck,pCards,dCards);
                System.out.print("Your Cards: ");
                for (int i = 0;i<pCards.size();i++){
                    if (i == pCards.size()-1){
                        System.out.print(pCards.get(i));
                        break;
                    }
                    System.out.print(pCards.get(i)+", ");
                }
                System.out.println();
                System.out.print("Dealer's Cards: ");
                for (int i = 0;i<dCards.size();i++){
                    if (i == dCards.size()-1){
                        System.out.print(dCards.get(i));
                        break;
                    }
                    System.out.print(dCards.get(i)+", ");
                }
                System.out.println();
                if (result==1){
                    return tieMessage(name,bet,balance);
                } else if (result==0){
                    return loseMessage(name, bet, balance);
                } else {
                    return winMessage(name, bet, balance);
                }
            }
        } else if (blackjackPlayer==0){
            return loseMessage(name, bet, balance);
        }
        return winMessage(name, bet, balance);

    }

    //Consequence of Stand option
    public static int optionStand(String[] cardDeck, List<String> pCards, List<String> dCards){
        //0 = loss
        //1 = tie
        //2 = win
        int pSum = countCardsSum(pCards);
        int dSum = countCardsSum(dCards);
        if(dSum>21) return 2;
        else if (dSum==21||dSum>pSum) return 0;
        else {
            dCards.add(cardDeck[new Random().nextInt(cardDeck.length)]);
            return optionStand(cardDeck,pCards,dCards);
        }
    }

    //Check if a certain hand(Player/Dealer) has a blackjack
    public static int checkIfBlackjack(List<String> cards){
        int sum = countCardsSum(cards);
        if (sum>21) return 0;
        else if (sum==21) return 2;
        else return 1;
    }

    //Count the numeral of the cards in a certain hand(Player/Dealer)
    public static int countCardsSum(List<String> cards){
        int sum = 0;
        int aceCount = 0;
        for (int i = 0;i<cards.size();i++){
            String currCard = cards.get(i);
            if(currCard.equals("K")||currCard.equals("Q")||currCard.equals("J")){
                sum+=10;
            }  else if (currCard.equals("A")){
                aceCount++;
            } else {
                sum+=Integer.parseInt(currCard);
            }
        }
        if(aceCount>1) {
            sum+=aceCount-1;
            aceCount=1;
        }
        if (aceCount==1){
            if (sum+11>21)sum+=1;
            else sum+=11;
        }
        return sum;
    }

    //Declare the bet amount
    public static int declareBetAmount(int currBalance){
        Scanner scanner = new Scanner(System.in);
        String balanceString = scanner.next();
        if (balanceString.endsWith("$")) balanceString = balanceString.substring(0,balanceString.length()-1);
        int balance = Integer.parseInt(balanceString);
        if (balance<=0||balance>currBalance){
            System.out.println("Enter a valid amount between 1 and " + currBalance);
            return declareBetAmount(currBalance);
        }
        return balance;
    }

    //Ask if player wants to leave the casino or player another one
    public static boolean askForNextGame(String name, int balance){
        if (balance==0) {
            return false;
        }
        System.out.println("Mr./Mrs. "+name+" you currently have " + balance + "$, what do you want to do now?");
        System.out.println("Type 'y' if you want to play another game");
        System.out.println("Type 'n' if you want to leave the casino");
        System.out.print("Your answer: ");
        Scanner scanner = new Scanner(System.in);
        char ans = scanner.next().charAt(0);
        if (ans=='n'){
            return false;
        } else if(ans=='y'){
            return true;
        } else {
            System.out.println("Please enter a valid answer");
            return askForNextGame(name,balance);
        }
    }

    //Leaving the casino
    public static void endGame(String name, int balance){
        System.out.println("Mr./Mrs. "+name +", you are leaving the casino with a balance of "+ balance+"$");
        System.out.println("Have a nice day and make sure to come back!");
    }

    //Tie message
    public static int tieMessage(String name, int bet, int balance){
        balance+=bet;
        System.out.println("Mr./Mrs. " + name+" the game ended in a tie");
        System.out.println("The amount you bet(" +bet+"$) was added to your balance");
        return balance;
    }

    //Win message
    public static int winMessage(String name, int bet, int balance){
        balance+=2*bet;
        System.out.println("Mr./Mrs. " + name+" you won the game. Congratulations!");
        System.out.println("Twice the amount you bet(" +bet+"$) was added to your balance");
        return balance;
    }

    //Lose message
    public static int loseMessage(String name, int bet, int balance){
        System.out.println("Mr./Mrs. " + name+" you lost the game");
        System.out.println("The amount you bet(" +bet+"$) was reduced from your balance");
        return balance;
    }


}