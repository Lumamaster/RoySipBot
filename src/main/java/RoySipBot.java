import sx.blah.discord.api.IDiscordClient;
import sx.blah.discord.api.events.EventDispatcher;
import sx.blah.discord.api.events.IListener;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageEvent;
import sx.blah.discord.handle.obj.*;
import sx.blah.discord.util.DiscordException;
import sx.blah.discord.util.MessageBuilder;
import sx.blah.discord.util.MissingPermissionsException;
import sx.blah.discord.util.RateLimitException;

import java.util.List;

public class RoySipBot extends BaseBot implements IListener<MessageEvent> {
    private Storage store = new Storage();
    private RNG rng = new RNG();

    RoySipBot(IDiscordClient discordClient) {
        super(discordClient);
        store.loadData();
        EventDispatcher dispatcher = discordClient.getDispatcher(); // Gets the client's event dispatcher
        dispatcher.registerListener(this); // Registers this bot as an event listener
    }

    /**
     * Called when the client receives a message.
     */
    @Override
    public void handle(MessageEvent event) {
        IMessage message = event.getMessage(); // Gets the message from the event object NOTE: This is not the content of the message, but the object itself
        IChannel channel = message.getChannel(); // Gets the channel in which this message was sent.
        IUser author = message.getAuthor();
        IGuild userroles = message.getGuild();
        List<IRole> rolelist = author.getRolesForGuild(userroles);
        try {
            if (message.getContent().startsWith("&")) //test if valid prefix
            {
                String withoutstart = message.getContent().substring(1, message.getContent().length());
                String[] m = withoutstart.split(" (?=(([^'\"]*['\"]){2})*[^'\"]*$)");
                int i;
                for (i = 0; i < m.length; i++) {
                    if (m[i].charAt(0) == '"') {
                        m[i] = m[i].substring(1, m[i].length() - 1);
                    }
                }
                for (i = 0; i < m.length; i++) {
                    System.out.println(m[i]);
                }
                if (!m[0].isEmpty()) {
                    if (m[0].equals("createchar"))// name
                    {
                        if (m.length != 2) {
                            printmessage("Invalid argument amount. Usage: &createchar \"name\"", channel);
                        } else {
                            if (store.hasCharacter(m[1])) {
                                printmessage("Character already exists.", channel);
                            } else {
                                store.addCharacter(m[1], new Character(m[1]));
                                store.getCharacter(m[1]).setOwner(author.getName());
                                store.saveData();
                                printmessage(store.getCharacter(m[1]).getStatus(), channel);
                            }
                        }
                    }
                    if (m[0].equals("deletechar"))// name
                    {
                        if (m.length != 2) {
                            printmessage("Invalid argument amount. Usage: &deletechar \"name\"", channel);
                        } else {
                            if (store.hasCharacter(m[1])) {
                                if (author.getName().equals(store.getCharacter(m[1]).getOwner())) {
                                    store.removeCharacter(m[1]);
                                    store.saveData();
                                    printmessage("Character removed.", channel);
                                } else {
                                    printmessage("You do not own this character.", channel);
                                }
                            } else {
                                printmessage("Character does not exist.", channel);
                            }
                        }
                    }
                    if (m[0].equals("charinfo"))// name
                    {
                        if (m.length != 2) {
                            printmessage("Invalid argument amount. Usage: &charinfo \"name\"", channel);
                        } else {
                            if (store.hasCharacter(m[1])) {
                                if (author.getName().equals(store.getCharacter(m[1]).getOwner())) {
                                    Character temp = store.getCharacter(m[1]);
                                    StringBuilder infomessage = new StringBuilder("***Name:*** ");
                                    infomessage.append(temp.getName());
                                    infomessage.append("\n***Owner:*** ");
                                    infomessage.append(temp.getOwner());
                                    infomessage.append("\n***Description:*** ");
                                    infomessage.append(temp.getDescription());
                                    printmessage(infomessage.toString(), channel);
                                } else {
                                    printmessage("You do not own this character.", channel);
                                }
                            } else {
                                printmessage("Character does not exist.", channel);
                            }
                        }
                    }
                    if (m[0].equals("changecharname"))// name name
                    {
                        if (m.length != 3) {
                            printmessage("Invalid argument amount. Usage: &changecharname \"before\" \"after\"", channel);
                        } else {
                            if (store.hasCharacter(m[1])) {
                                if (author.getName().equals(store.getCharacter(m[1]).getOwner())) {
                                    store.getCharacter(m[1]).changeName(m[2]);
                                    store.addCharacter(m[2], store.getCharacter(m[1]));
                                    store.removeCharacter(m[1]);
                                    store.addCharacter(m[1], store.getCharacter(m[1]));
                                    store.saveData();
                                    printmessage(store.getCharacter(m[2]).getStatus(), channel);
                                } else {
                                    printmessage("You do not own this character.", channel);
                                }
                            } else {
                                printmessage("Character does not exist.", channel);
                            }
                        }
                    }
                    if (m[0].equals("setchardescription"))// name "desc"
                    {
                        if (m.length != 3) {
                            printmessage("Invalid argument amount. Usage: &setchardescription \"name\" \"description\"", channel);
                        } else {
                            if (store.hasCharacter(m[1])) {
                                if (author.getName().equals(store.getCharacter(m[1]).getOwner())) {
                                    store.getCharacter(m[1]).setDescription(m[2]);
                                    store.saveData();
                                    printmessage(store.getCharacter(m[1]).getStatus(), channel);
                                } else {
                                    printmessage("You do not own this character.", channel);
                                }
                            } else {
                                printmessage("Character does not exist.", channel);
                            }
                        }
                    }
                    if (m[0].equals("listchars")) {
                        if (m.length != 1) {
                            printmessage("Invalid argument amount. Usage: &listchars", channel);
                        } else {
                            Character[] temp = store.getcharlist();
                            int a;
                            StringBuilder t = new StringBuilder("List of characters: \n");
                            for (a = 0; a < temp.length; a++) {
                                t.append(temp[a].getName()).append("\n");
                            }
                            printmessage(t.toString(), channel);
                        }
                    }
                    if (m[0].equals("roll")) {
                        if (m.length != 2) {
                            printmessage("Usage: &roll (optional: dice amount)d(num of die faces)  (ex: &roll 8d100)", channel);
                        } else {
                            int amount = 1;
                            int num = 1;
                            if (java.lang.Character.isDigit(m[1].charAt(0))) {
                                String[] dice = m[1].split("d",2);
                                if (dice[0].matches("[0-9]+")) {
                                    amount = Integer.parseInt(dice[0]);
                                } else {
                                    printmessage("Invalid dice amount inputted. Ensure it is a number.", channel);
                                    return;
                                }
                                if (dice[1].matches("[0-9]+")) {
                                    num = Integer.parseInt(dice[1]);
                                } else {
                                    printmessage("Invalid dice face count inputted. Ensure it is a number.", channel);
                                    return;
                                }
                                int a;
                                String mess = author.mention() + ": `" + m[1] + "`" + " = (";
                                int total = 0;
                                int temp;
                                for (a = 0; a < amount; a++) {
                                    temp = rng.roll(num);
                                    total += temp;
                                    mess += temp;
                                    if (a < amount-1) {
                                        mess += "+";
                                    }
                                }
                                mess += ") = ";
                                mess += total;
                                printmessage(mess, channel);
                            } else if (m[1].charAt(0) == 'd') {
                                if (m[1].substring(1).matches("[0-9]+")) {
                                    int asdf = Integer.parseInt(m[1].substring(1));
                                    int res = rng.roll(asdf);
                                    String mess = author.mention() + ": `" + m[1] + "`" + " = (" + res + ") = " + res;
                                    printmessage(mess, channel);
                                } else {
                                    printmessage("Invalid dice face count inputted. Ensure it is a number.", channel);
                                }
                            } else {
                                printmessage("Usage: &roll (optional: dice amount)d(num of die faces)  (ex: &roll 8d100)", channel);
                            }
                        }
                    }
                }
            }
        } catch (RateLimitException e) { // RateLimitException thrown. The bot is sending messages too quickly!
            System.err.print("Sending messages too quickly!");
            e.printStackTrace();
        } catch (DiscordException e) { // DiscordException thrown. Many possibilities. Use getErrorMessage() to see what went wrong.
            System.err.print(e.getErrorMessage()); // Print the error message sent by Discord
            e.printStackTrace();
        } catch (MissingPermissionsException e) { // MissingPermissionsException thrown. The bot doesn't have permission to send the message!
            System.err.print("Missing permissions for channel!");
            e.printStackTrace();
        }
    }

    private void printmessage(String m, IChannel c) {
        new MessageBuilder(this.client).withChannel(c).withContent(m).build();
    }
}