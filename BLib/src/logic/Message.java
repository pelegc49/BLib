package logic;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * This class is used for communication between the client and server.
 * It encapsulates a command and its associated arguments, which can be passed between the client and server during interaction.
 * The class implements {@link Serializable} to allow the object to be serialized for network transmission.
 */
public class Message implements Serializable {

    private String command;
    private List<Object> arguments;

    /**
     * Constructs a Message object with a given command and an unlimited number of arguments.
     * 
     * @param command the command to be sent, typically representing an action or operation to be performed
     * @param args the arguments associated with the command
     */
    public Message(String command, Object ... args) {
        this.command = command;
        arguments = new ArrayList<Object>();
        // Adding each argument to the list of arguments
        for (Object o : args) {
            arguments.add(o);
        }
    }

	public String getCommand() {
		return command;
	}

	public List<Object> getArguments() {
		return arguments;
	}
	
	@Override
	public String toString() {
		return command + Arrays.deepToString(arguments.toArray());
	}
	
	
}
