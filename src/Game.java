import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;

/**
 * This class is the main class of the "Tube Game" application.
 * Find your way to the university campus!
 * Tube game is a command line-based game in which you have to get
 * from home to the university campus using the London Underground.
 * <p>
 * To play this game, create an instance of this class and call the "play"
 * method.
 * <p>
 * This main class creates and initialises all the others: it creates all
 * stations, creates the parser and starts the game.  It also evaluates and
 * executes the commands that the parser returns.
 *
 * @author Michael Kölling and David J. Barnes and Rares I. Tamasanu
 * @version 2021.12.04
 */

public class Game {
    public static void main(String[] args) {
        Game game = new Game();
        game.play();
    }

    private Parser parser;
    private Player player;

    private StationsGraph tubeMap;
    private HashSet<Card> cards;
    private HashSet<Drink> drinks;
    private boolean transactionInProgress;
    private float currentItemPrice;
    private Entity itemToBePutInBackpack;
    private boolean justStarted;

    private boolean winner;

    /**
     * Create the game and initialise its internal map.
     */
    public Game() {
        player = new Player();
        parser = new Parser();
        createStations();
        createItems();

        justStarted = true;
        transactionInProgress = false;
        winner = false;
    }

    /**
     * Create all the stations and transport links.
     */
    private void createStations() {
        Station victoria, greenPark, oxfordCircus, tottenham, holborn, coventGarden,
                leicesterSq, temple, chanceryLane, bondStreet, strandCampus, maughan,
                coventGardenShops, oxfordShops, tottenhamPubs, leicesterPubs, magicTransporter;
        tubeMap = new StationsGraph();

        // create the stations
        victoria = new Station("Victoria",
                "This is home. You are far away from your destination (the uni campus)");
        greenPark = new Station("GreenPark",
                "This is a good interchange station, but you're still far away from campus.");
        oxfordCircus = new Station("OxfordCircus",
                "This is a good interchange station, but you're still far away from campus.");
        tottenham = new Station("TottenhamCourtRoad",
                "You are slightly closer to campus. This is a popular station for pubs.");
        holborn = new Station("Holborn",
                "Get off here for the Strand campus");
        coventGarden = new Station("CoventGarden",
                "You are slightly closer to campus. This is a popular station for shopping.");
        leicesterSq = new Station("LeicesterSquare",
                "You are still quite far from uni. This is a popular station for pubs and clubs.");
        temple = new Station("Temple",
                "Get off here for the Strand campus");
        chanceryLane = new Station("ChanceryLane",
                "This is your stop for the Maughan Library, a good place for after-school studying");
        bondStreet = new Station("BondStreet",
                "You are lost, find help.");
        magicTransporter = new Station("MagicTransporter", "Poof!");

        // exits:
        strandCampus = new Station("StrandCampus",
                "Congratulations! You've made it to the King's Strand Campus. \n Use tapCard studentCard accessGateCampus to enter and win!", "exit");
        maughan = new Station("MaughanLibrary",
                "This is the Maughan Library, a good place for after-school studying", "exit");
        coventGardenShops = new Station("Shops",
                "Here you can do some shopping for clothes etc.", "exit");
        oxfordShops = new Station("Shops",
                "Here you can do some shopping for clothes etc.", "exit");
        tottenhamPubs = new Station("Pubs",
                "Good for drinking.", "exit");
        leicesterPubs = new Station("Pubs",
                "Good for drinking.", "exit");


        // initialise transport links and the tube map
        tubeMap.addEdge(victoria, temple);
        tubeMap.addEdge(victoria, greenPark);
        tubeMap.addEdge(greenPark, oxfordCircus);
        tubeMap.addEdge(greenPark, leicesterSq);
        tubeMap.addEdge(greenPark, bondStreet);
        tubeMap.addEdge(oxfordCircus, tottenham);
        tubeMap.addEdge(oxfordCircus, bondStreet);
        tubeMap.addEdge(tottenham, holborn);
        tubeMap.addEdge(holborn, chanceryLane);
        tubeMap.addEdge(holborn, coventGarden);
        tubeMap.addEdge(coventGarden, leicesterSq);

        tubeMap.addEdge(temple, strandCampus);
        tubeMap.addEdge(holborn, strandCampus);
        tubeMap.addEdge(coventGarden, coventGardenShops);
        tubeMap.addEdge(leicesterSq, leicesterPubs);
        tubeMap.addEdge(tottenham, tottenhamPubs);
        tubeMap.addEdge(chanceryLane, maughan);
        tubeMap.addEdge(oxfordCircus, oxfordShops);

        tubeMap.addDirectedEdge(bondStreet, magicTransporter);
        tubeMap.addDirectedEdge(magicTransporter, victoria);
        tubeMap.addDirectedEdge(magicTransporter, greenPark);
        tubeMap.addDirectedEdge(magicTransporter, oxfordCircus);
        tubeMap.addDirectedEdge(magicTransporter, tottenham);
        tubeMap.addDirectedEdge(magicTransporter, holborn);
        tubeMap.addDirectedEdge(magicTransporter, coventGarden);
        tubeMap.addDirectedEdge(magicTransporter, leicesterSq);
        tubeMap.addDirectedEdge(magicTransporter, temple);
        tubeMap.addDirectedEdge(magicTransporter, chanceryLane);

        victoria.setNeighbours(tubeMap);
        greenPark.setNeighbours(tubeMap);
        oxfordCircus.setNeighbours(tubeMap);
        tottenham.setNeighbours(tubeMap);
        holborn.setNeighbours(tubeMap);
        coventGarden.setNeighbours(tubeMap);
        chanceryLane.setNeighbours(tubeMap);
        bondStreet.setNeighbours(tubeMap);
        temple.setNeighbours(tubeMap);
        leicesterSq.setNeighbours(tubeMap);
        strandCampus.setNeighbours(tubeMap);
        maughan.setNeighbours(tubeMap);
        coventGardenShops.setNeighbours(tubeMap);
        oxfordShops.setNeighbours(tubeMap);
        tottenhamPubs.setNeighbours(tubeMap);
        leicesterPubs.setNeighbours(tubeMap);
        magicTransporter.setNeighbours(tubeMap);

        player.setLocation(victoria); // start game at home in Victoria
    }

    /**
     * Create all the items and put them on the map.
     */
    private void createItems() {
        // create cards
        cards = new HashSet<>();
        Card oysterCard, studentCard, creditCard;

        oysterCard = new Card("oysterCard", 12.5F);
        studentCard = new Card("studentCard", -1); //can't have a balance on student card
        creditCard = new Card("creditCard", 25);
        oysterCard.setQuantity(1);
        studentCard.setQuantity(1);
        creditCard.setQuantity(1);

        cards.add(oysterCard);
        cards.add(studentCard);
        cards.add(creditCard);
        tubeMap.getStation("Victoria").addItem(oysterCard);
        tubeMap.getStation("Victoria").addItem(studentCard);
        tubeMap.getStation("Victoria").addItem(creditCard);

        // create available drinks
        drinks = new HashSet<>();
        Drink beer, cocktail;

        beer = new Drink("beer", 4, "alcoholic");
        cocktail = new Drink("cocktail", 8, "alcoholic");

        drinks.add(beer);
        drinks.add(cocktail);

        Entity laptop, book, clothes, chair;

        // create laptop
        laptop = new Entity("laptop", 2, true);
        laptop.setQuantity(1);
        tubeMap.getStation("MaughanLibrary").addItem(laptop);

        // create book
        book = new Entity("book", 0.5F, true);
        tubeMap.getStation("MaughanLibrary").addItem(book);

        // create clothes
        clothes = new Entity("clothes", 2, true, 15);
        tubeMap.getStation("Shops").addItem(clothes);

        // create items that can't be picked up
        chair = new Entity("chair", 6, false);
        tubeMap.getStation("MaughanLibrary").addItem(chair);

    }

    /**
     * Main play routine.  Loops until end of play.
     */
    public void play() {
        printWelcome();

        // Enter the main command loop.  Here we repeatedly read commands and
        // execute them until the game is over.

        boolean finished = false;
        boolean isCompletelyBroke = false;
        while (!finished && !winner) {
            Command command = parser.getCommand();
            finished = processCommand(command);
           // isCompletelyBroke = player.isCompletelyBroke();
        }
//        if (isCompletelyBroke)
//            System.out.println("You're too broke to keep playing.");
        System.out.println("Thank you for playing. Good bye.");
    }

    /**
     * Print out the opening message for the player.
     */
    private void printWelcome() {
        System.out.println();
        System.out.println("Welcome to the World of Zuul!");
        System.out.println("World of Zuul is a new, incredibly boring adventure game.");
        System.out.println("Type 'help' if you need help.");
        System.out.println();
        System.out.println(player.getLocation().getLongDescription());
        System.out.println("Nearby stations: " + player.getLocation().getNeighboursString());
    }

    /**
     * Given a command, process (that is: execute) the command.
     *
     * @param command The command to be processed.
     * @return true If the command ends the game, false otherwise.
     */
    private boolean processCommand(Command command) {
        boolean wantToQuit = false;


        if (command.isUnknown()) {
            System.out.println("I don't know what you mean...");
            return false;
        }

        String commandWord = command.getCommandWord();
        if (commandWord.equals("help")) {
            printHelp();
        } else if (commandWord.equals("go")) {
            goStation(command);
        } else if (commandWord.equals("exit")) {
            exitStation(command);
        } else if (commandWord.equals("back")) {
            goBack();
        } else if (commandWord.equals("drink")) {
            doDrinking(command);
        } else if (commandWord.equals("tapCard")) {
            tapCard(command, currentItemPrice, itemToBePutInBackpack);
        } else if (commandWord.equals("take")) {
            takeItem(command);
        } else if (commandWord.equals("drop")) {
            dropItem(command);
        } else if (commandWord.equals("buy")) {
            buyItem(command);
        } else if (commandWord.equals("whatsinmybag")) {
            whatsInMyBag();
        } else if (commandWord.equals("quit")) {
            wantToQuit = quit(command);
        }
        // else command not recognised.
        return wantToQuit;
    }

    // implementations of user commands:

    /**
     * Print out some help information.
     * Here we print some stupid, cryptic message and a list of the
     * command words.
     */
    private void printHelp() {
        System.out.println("You are lost. You are alone. You wander");
        System.out.println("around at the university.");
        System.out.println();
        System.out.println("Your command words are:");
        parser.showCommands();
    }

    /**
     * Try to travel to a station. If there is a transport link and the player has paid the transport fare,
     * change player's location to that destination, otherwise print an error message.
     * @param command station to travel to
     */
    private void goStation(Command command) {
        Station currentStation = player.getLocation();
        transactionInProgress = false;

        if (!command.hasSecondWord()) {
            // if there is no second word, we don't know where to go...
            System.out.println("Go where?");
            System.out.println("Nearby stations: " + player.getLocation().getNeighboursString());
            return;
        }

        String destination = command.getSecondWord();

        if (destination.equals("MagicTransporter")) {
            magicallyTransport("MagicTransporter", "");
            return;
        }

        // Try to leave current station.

        HashSet<Station> neighbours = currentStation.getNeighbours();
        Station nextStation = null;
        for (Station neighbour : neighbours)
            if (neighbour.getName().equals(destination)) {
                nextStation = neighbour;
                break;
            }

        if (currentStation.isExit())
            if (nextStation == null) {  // if player wants to go to a station that isn't the nearest one [NOT VALID, HE HAS TO TAKE TRAIN THERE]
                System.out.println("You are not at a train station! Use \"go [nearest train station]\"");
                return;
            } else {   // if player wants to go to the nearest station [VALID]
                player.setPreviousLocation(currentStation);
                player.setLocation(nextStation);
                System.out.println(player.getLocation().getLongDescription());
                System.out.println("Nearby stations: " + player.getLocation().getNeighboursString());
                return;
            }


        if (!player.getHasPaidTubeEntrance()) {
            System.out.println("You haven't paid to enter the train station yet.");
            System.out.println("Use tapCard oysterCard/creditCard accessGateTube");
            return;
        }

        if (destination.equals(currentStation.getName())) {
            System.out.println("You are already here!");
            return;
        }

        if (nextStation == null) {
            System.out.println("There is no line to that station!");
            return;
        }

        if (player.isDrunk()) {
            magicallyTransport(currentStation.getName(), destination);
            return;
        }


        player.setPreviousLocation(currentStation);
        player.setLocation(nextStation);
        System.out.println(player.getLocation().getLongDescription());
        System.out.println("Nearby stations: " + player.getLocation().getNeighboursString());
        if (!player.getLocation().getExits().isEmpty())
            System.out.println("Available exits: " + player.getLocation().getExitsString());
    }

    /**
     * Try to exit a station. If there is an exit at the current station, change player's location
     * to that exit.
     * @param command exit chosen
     */
    public void exitStation(Command command) {
        transactionInProgress = false;
        HashSet<Station> exits = player.getLocation().getExits();
        if (!command.hasSecondWord()) {
            // if there is no second word, we don't know where to go...
            if (exits.isEmpty()) {
                System.out.println("Nothing to do here...No exits");
                return;
            }
            System.out.println("Available exits: " + player.getLocation().getExitsString());
            System.out.println("Please use \"exit [desired exit]\"");
            return;
        }

        String destination = command.getSecondWord();
        // Try to leave current station.
        Station nextStation = null;
        for (Station exit : exits)
            if (exit.getName().equals(destination)) {
                nextStation = exit;
                break;
            }

        if (nextStation == null) {
            System.out.println("Nothing to do here...");
            return;
        }
        player.setPreviousLocation(player.getLocation());
        player.setLocation(nextStation);
        System.out.println(player.getLocation().getLongDescription());
        System.out.println("Nearby stations: " + player.getLocation().getNeighboursString());
        player.setHasPaidTubeEntrance(false);

    }

    /**
     * Transport player to previous location, whether that is a station or an exit.
     * If the player has just started the game and there is no previous location, print an error message.
     */
    private void goBack() {
        //transactionInProgress = false;

        if (player.getPreviousLocation() == null) {
            System.out.println("Can't go back!");
            return;
        }


        Station aux;
        aux = player.getLocation();
        player.setLocation(player.getPreviousLocation());
        player.setPreviousLocation(aux);

        System.out.println("Going back... to " + player.getLocation().getName() + "!");
        System.out.println(player.getLocation().getLongDescription());
        System.out.println("Nearby stations: " + player.getLocation().getNeighboursString());
        if (!player.getLocation().getExits().isEmpty())
            System.out.println("Available exits: " + player.getLocation().getExitsString());
    }

    /**
     * Try to buy an item. If the item doesn't exist, print an error message.
     *
     * @param command item to be bought
     */
    private void buyItem(Command command) {
        HashSet<Entity> itemsToBeBought = new HashSet<>();
        HashSet<Entity> items = tubeMap.getStation(player.getLocation().getName()).getItemsAvailable();
        if (items == null) {
            System.out.println("No items here");
            return;
        }
        items.forEach(item -> {
            if (item.isPurchasable())
                itemsToBeBought.add(item);
        });

        if (itemsToBeBought.isEmpty()) {
            System.out.println("Can't buy anything here");
            return;
        }

        if (!command.hasSecondWord()) {
            System.out.println("Which item do you want to buy?");
            itemsToBeBought.forEach(item -> {
                System.out.print(item.getName() + " ");
            });
            System.out.println();
            return;
        }

        String chosenItemString = command.getSecondWord();
        Entity chosenItem = null;
        for (Entity item : itemsToBeBought)
            if (item.getName().equals(chosenItemString))
                chosenItem = item;

        if (chosenItem == null) {
            System.out.println("This item is not here");
            return;
        }

        System.out.println("You have chosen to buy " + chosenItem.getName());
        System.out.println("That will be £" + chosenItem.getPrice() + " please.");
        System.out.println(" [Use \"tapCard [whichCard] [whereToTap? = cardMachine]\"] to pay.");
        transactionInProgress = true;

        currentItemPrice = chosenItem.getPrice();
        itemToBePutInBackpack = chosenItem;

    }

    /**
     * Tries to identify the item to be put in backpack.
     * If found, puts it in the backpack via the player command, otherwise prints a message.
     *
     * @param command item to be put in backpack
     */
    private void takeItem(Command command) {
        HashSet<Entity> items = tubeMap.getStation(player.getLocation().getName()).getItemsAvailable();
        HashSet<Entity> availableItems = new HashSet<>();
        if (items == null) {
            System.out.println("No items here");
            return;
        }

        items.forEach(item -> {
            if (!item.isPurchasable())
                availableItems.add(item);
        });

        if (availableItems.isEmpty()) {
            System.out.println("No items here");
            return;
        }

        if (!command.hasSecondWord()) {
            System.out.println("Which item do you want to take with you?");
            availableItems.forEach(item -> {
                System.out.print(item.getName() + " ");
            });
            System.out.println();
            return;
        }

        String chosenItemString = command.getSecondWord();
        Entity chosenItem = null;
        for (Entity item : availableItems)
            if (item.getName().equals(chosenItemString))
                chosenItem = item;

        if (chosenItem == null) {
            System.out.println("This item is not here");
            return;
        }


        if (!player.putItemInBackpack(chosenItem)) return;

        System.out.println("This item: " + chosenItemString + " is now in your backpack.");
        System.out.println("Available room in your backpack: " + player.getRemainingWeight() + "kg.");
    }

    /**
     * Tries to identify the item to be taken out of backpack.
     * If found, takes it out of the backpack via the player command, otherwise prints a message.
     *
     * @param command item to be taken out of backpack
     */
    private void dropItem(Command command) {
        ArrayList<Entity> carriedItems = player.getItemsCarried();

        if (carriedItems.isEmpty()) {
            System.out.println("Nothing to drop. You're not carrying anything!");
            return;
        }

        if (!command.hasSecondWord()) {
            System.out.println("What do you want to take out of your backpack?");
            carriedItems.forEach(item -> {
                System.out.print(item.getName() + " ");
            });
            System.out.println();
            return;
        }

        String chosenItemString = command.getSecondWord();
        Entity chosenItem = null;
        for (Entity item : carriedItems)
            if (item.getName().equals(chosenItemString))
                chosenItem = item;

        if (chosenItem == null) {
            System.out.println("You're not carrying that item!");
            return;
        }

        player.dropItem(chosenItem);
        System.out.println("You've taken " + chosenItemString + " out of your backpack.");
        System.out.println("Available room in your backpack: " + player.getRemainingWeight() + "kg.");


    }

    /**
     * Tries to consume a drink. If player is not at the pubs or the drink doesn't exist,
     * prints an error message. If everything is ok then proceed to payment.
     *
     * @param command chosen drink
     */
    private void doDrinking(Command command) {
        if (!player.getLocation().getName().equals("Pubs")) {
            System.out.println("You are not at the pubs!");
            return;
        }

        if (!command.hasSecondWord()) {// if there is no second word, prompt user to choose a drink
            System.out.print("What's it gonna be? Available drinks: ");
            drinks.forEach(drink -> {
                System.out.print(drink.getName() + " ");
            });
            System.out.println();
            return;
        }

        String chosenDrinkString = command.getSecondWord();
        Drink chosenDrink = null;

        for (Drink drink : drinks)
            if (drink.getName().equals(chosenDrinkString))
                chosenDrink = drink;

        if (chosenDrink == null) {
            System.out.println("I don't know what you mean");
            return;
        }

        System.out.println("You have chosen to drink a " + chosenDrink.getName());
        System.out.println("That will be £" + chosenDrink.getPrice() + " please.");
        System.out.println(" [Use \"tapCard [whichCard] [whereToTap? = cardMachine]\"] to pay.");
        transactionInProgress = true;

        currentItemPrice = chosenDrink.getPrice();
        itemToBePutInBackpack = chosenDrink;
    }

    /**
     * Handles payment of the tube fare. The tube fare is a fixed 2.5 pounds. This method also checks
     * if the player has already paid this fare after exiting a station or not.
     *
     * @param card card chosen to pay with
     */
    private void makeTubePayment(Card card) {
        if (player.getHasPaidTubeEntrance()) {
            System.out.println("You already paid to enter the tube!");
            return;
        }
        if (!justStarted && player.isOnTube()) {
            System.out.println("You are already on the tube!");
            return;
        }
        if (player.getLocation().isExit()) {
            System.out.println("You are not at a station!");
            return;
        }

        if (!card.bill(2.5F)) return;  // not enough money to bill card

        justStarted = false;
        player.setHasPaidTubeEntrance(true);
        System.out.println("Payment successful, you're on the tube now. \n Remaining balance: £" +
                card.getRemainingBalance() + ".");
    }

    /**
     * Tries to tap a card on a reader to execute a payment.
     * And follows up with obtaining the item we just paid for.
     *
     * This method is pretty bad and needs cleanup
     *
     * @param command card to be tapped and reader to be touched
     * @param amount amount to be billed
     * @param itemToBePutInBackpack this param is relevant if the outcome of the payment is obtaining an item
     */
    private void tapCard(Command command, float amount, Entity itemToBePutInBackpack) {
        ArrayList<Entity> carriedItems = player.getItemsCarried();
        if (!command.hasSecondWord()) {
            System.out.print("Don't know which card to tap. Available cards: ");
            cards.forEach(card -> {
                if (carriedItems.contains(card))
                    System.out.print(card.getName() + " ");
            });
            System.out.println();
            return;
        }

        if (!command.hasThirdWord()) {
            System.out.println("Don't know where to tap this card. Perhaps you wanted to tap it on" +
                    " a cardMachine, accessGateTube or accessGateCampus?");
            return;
        }

        Card selectedCard = null;

        // try to find chosen card
        String cardNameString = command.getSecondWord();
        for (Card card : cards)
            if (card.getName().equals(cardNameString))
                selectedCard = card;

        if (selectedCard == null || !carriedItems.contains(selectedCard)) {
            System.out.println("You don't have that card. Cards on you: ");
            cards.forEach(card -> {
                if (carriedItems.contains(card))
                    System.out.print(card.getName() + " ");
            });
            System.out.println();
            if (player.getLocation().getName().equals("Victoria"))
                System.out.println("Use \"take [item]\" to pick up a card.");

            return;
        }

        // try to find card reader (target)
        String target = command.getThirdWord();
        if (!target.equals("cardMachine") && !target.equals("accessGateTube") && !target.equals("accessGateCampus")) {
            System.out.println("I'm not sure where you want to tap this card");
            return;
        }

        if (target.equals("accessGateCampus")) {
            if (!player.isAtCampus()) {
                System.out.println("You're not at Strand yet. Can't enter building");
                return;
            }
            if (!selectedCard.getName().equals("studentCard")) {
                System.out.println("u can only tap ur student card on that gate");
                return;
            }
            System.out.println("CONGRATS YOU MADE IT AND YOU WON !!!");
            winner = true;
            return;
        }

        if (target.equals("accessGateTube")) {
            if (selectedCard.getName().equals("studentCard")) {
                System.out.println("cant pay with a student card");
                return;
            }
            makeTubePayment(selectedCard);
            return;
        }

        if (!selectedCard.getName().equals("creditCard")) {
            System.out.println("You can't use your oyster card or student card to pay here.");
            return;
        }

        if (!transactionInProgress) {
            System.out.println("no ongoing transaction");
            return;
        }


        // handle payment
        if (!selectedCard.bill(amount)) return;  // not enough money to bill card
        System.out.println("Payment successful, remaining balance on " + selectedCard.getName() + ": £" +
                selectedCard.getRemainingBalance() + ".");
        transactionInProgress = false;  // transaction ended

        if (itemToBePutInBackpack.isAbleToBePickedUp()) {
            if (!player.putItemInBackpack(itemToBePutInBackpack)) return; // check if we have enough space in backpack
            itemToBePutInBackpack.setNonPurchasable(); // once we bought and picked up an item we can't purchase it again

            System.out.println("This item: " + itemToBePutInBackpack.getName() + " is now in your backpack.");
            System.out.println("Available room in your backpack: " + player.getRemainingWeight() + "kg.");
        }

        // in case you just paid for an alcoholic drink => you become drunk
        if (itemToBePutInBackpack instanceof Drink && ((Drink) itemToBePutInBackpack).isAlcoholic()) {
            System.out.println("You are now drunk!");
            player.setDrunk(true);
        }


    }

    /**
     * Transports player to a random destination.
     * Used for the magic transporter as well as when the player is drunk.
     *
     * If the random function returns the same station as the desired destination (when drunk), run the method again.
     *
     * @param source  where the method was called from
     * @param desiredDestination
     */
    private void magicallyTransport(String source, String desiredDestination) {
        HashSet<Station> neighbours = tubeMap.getStation(source).getNeighbours();

        // convert HashSet to an array
        Station[] neighboursArray = neighbours.toArray(new Station[neighbours.size()]);

        // generate a random number
        Random rndm = new Random();

        // this will generate a random number between 0 and
        // HashSet.size - 1
        int rndmNumber = rndm.nextInt(neighbours.size());

        // get the element at random number index
        Station randomStation = neighboursArray[rndmNumber];

        if (randomStation.getName().equals(desiredDestination)) {
            magicallyTransport(source, desiredDestination);
            return;
        }

        player.setPreviousLocation(player.getLocation());
        player.setLocation(randomStation);
        if (source.equals("MagicTransporter"))
            System.out.println("Poof! You've been magically transported!");
        else System.out.println("You're drunk and you took the train in the wrong direction!");
        System.out.println(player.getLocation().getLongDescription());
        System.out.println("Nearby stations: " + player.getLocation().getNeighboursString());
    }

    /**
     * Prints a list of the items in your bag or an error message if there is none.
     */
    private void whatsInMyBag() {
        if (player.getItemsCarried().isEmpty()) {
            System.out.println("Bag empty");
            return;
        }

        System.out.println("Items currently in your bag: ");
        player.getItemsCarried().forEach(item -> {
            System.out.print(item.getName() + " ");
        });
        System.out.println();
    }

    /**
     * "Quit" was entered. Check the rest of the command to see
     * whether we really quit the game.
     *
     * @return true, if this command quits the game, false otherwise.
     */
    private boolean quit(Command command) {
        if (command.hasSecondWord()) {
            System.out.println("Quit what?");
            return false;
        } else {
            return true;  // signal that we want to quit
        }
    }

}
