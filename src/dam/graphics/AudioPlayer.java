package dam.graphics;

import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;

/**
 * Created by Emil Damsbo on 12-01-2017.
 */
public class AudioPlayer implements LineListener {
    public enum AUDIO {
        ERROR("src\\dam\\graphics\\Error.wav"),
        MOVE("src\\dam\\graphics\\On_move.wav"),
        WON("src\\dam\\graphics\\Game_won.wav");

        private String text;

        AUDIO(String text) {
            this.text = text;
        }

        public String getText() {
            return this.text;
        }

        public static AUDIO fromString(String text) {
            if (text != null) {
                for (AUDIO a : AUDIO.values()) {
                    if (text.equalsIgnoreCase(a.text)) {
                        return a;
                    }
                }
            }
            return null;
        }
    }


    public AudioPlayer(AudioPlayer.AUDIO clipToPlay){
        play(clipToPlay.getText());
    }

    boolean playCompleted;

    public synchronized void play(final String url) {
        // http://www.codejava.net/coding/how-to-play-back-audio-in-java-with-examples
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    File audioFile = new File(url);
                    AudioInputStream audioStream = AudioSystem.getAudioInputStream(audioFile);

                    AudioFormat format = audioStream.getFormat();
                    DataLine.Info info = new DataLine.Info(Clip.class, format);

                    Clip audioClip = (Clip) AudioSystem.getLine(info);

                    audioClip.open(audioStream);
                    audioClip.start();

                    while (!playCompleted) {
                        // wait for the playback completes
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException ex) {
                            ex.printStackTrace();
                        }
                    }

                } catch(UnsupportedAudioFileException aud) {
                    System.out.println("File format is unsupported");
                } catch(IOException io) {
                    System.out.println("File could not be found, error code: " + io.getMessage());
                } catch(LineUnavailableException line) {
                    System.out.println("Clip could not be obtained");
                }
            }
        }).start();

    }

    @Override
    public void update(LineEvent event) {
        LineEvent.Type type = event.getType();

        if (type == LineEvent.Type.START) {
            System.out.println("Playback started.");

        } else if (type == LineEvent.Type.STOP) {
            playCompleted = true;
            System.out.println("Playback completed.");
        }
    }
}