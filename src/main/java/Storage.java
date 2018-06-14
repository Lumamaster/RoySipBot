import java.io.*;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class Storage {
    private Map charhash = Collections.synchronizedMap(new HashMap<String, Character>(200));
    void addCharacter(String s, Character c) { charhash.put(s, c); }
    boolean hasCharacter(String s) { return charhash.containsKey(s); }
    Character getCharacter(String s) { return (Character)charhash.get(s); }
    void removeCharacter(String s) {
        File f = new File(System.getProperty("user.dir") + "/characters/" + s);
        if (f.delete()) {
            System.out.println("Character " + s + " deleted.");
        } else {
            System.out.println("Character failed to be deleted.");
        }
        charhash.remove(s);
    }
    Character[] getcharlist() {
        Collection<Character> tempchar = charhash.values();
        Character[] chararray = tempchar.toArray(new Character[0]);
        return chararray;
    }
    void saveData()
    {
        Collection<Character> tempchar = charhash.values();
        Character[] chararray = tempchar.toArray(new Character[0]);
        int i;
        try {
            for (i = 0; i < chararray.length; i++) {
                File f = new File(System.getProperty("user.dir") + "/characters/" + chararray[i].getName());
                f.getParentFile().mkdirs();
                if (!f.exists()) {
                    f.createNewFile();
                }
                FileOutputStream fout = new FileOutputStream(System.getProperty("user.dir") + "/characters/" + chararray[i].getName());
                ObjectOutputStream oout = new ObjectOutputStream(fout);
                oout.writeObject(chararray[i]);
                oout.close();
            }
        } catch (IOException e) {
            System.out.println(e);
        }
    }
    void loadData()
    {
        try {
            File[] charfiles = new File(System.getProperty("user.dir") + "/characters/").listFiles();
            for (File file : charfiles) {
                FileInputStream fin = new FileInputStream(System.getProperty("user.dir") + "/characters/" + file.getName());
                ObjectInputStream oin = new ObjectInputStream(fin);
                charhash.put(file.getName(), oin.readObject());
                oin.close();
            }
        } catch (IOException e) {
            System.out.println(e);
        } catch (ClassNotFoundException e) {
            System.out.println(e);
        } catch (NullPointerException e) {
            File chars = new File(System.getProperty("user.dir") + "/characters/" + "dummy.char");
            chars.mkdirs();
        }
        Collection<Character> tempchar = charhash.values();
        Character[] chararray = tempchar.toArray(new Character[0]);
        int i;
        System.out.println("Characters loaded: " + charhash.size());
        for (i = 0; i < chararray.length; i++) {
            System.out.println(chararray[i].getName());
        }
    }
}
