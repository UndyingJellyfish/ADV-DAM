package dam.graphics;

import javax.sound.sampled.*;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by Emil Damsbo on 12-01-2017.
 * Member primarily responsible for file: Emil Damsbo
 */
public class AudioPlayer implements LineListener {
    public enum AUDIO {
        // binds enum values to a string, which is the relative file path of sound files
        ERROR("/Sounds/Error.wav"),
        MOVE("/Sounds/On_move.wav"),
        WON("/Sounds/Game_won.wav");

        final private String text;

        AUDIO(String text) {
            this.text = text;
        }

        public String getText() {
            return this.text;
        }
    }


    public AudioPlayer(AudioPlayer.AUDIO clipToPlay) {
        play(clipToPlay.getText());
    }

    private boolean playCompleted;

    private synchronized void play(final String url) {
        // http://www.codejava.net/coding/how-to-play-back-audio-in-java-with-examples
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    // the input stream needs to be gotten as resoruce, else it won't properly pack into the .jar
                    // AudioInput needs to support a mark/reset-functionality, so we need to use a BufferedInputStream
                    // which marks resource as using mark/reset
                    InputStream bufferedIn = new BufferedInputStream(getClass().getResourceAsStream(url));
                    AudioInputStream audioStream = AudioSystem.getAudioInputStream(bufferedIn);

                    DataLine.Info info = new DataLine.Info(Clip.class, audioStream.getFormat());

                    Clip audioClip = (Clip) AudioSystem.getLine(info);

                    // plays the audio clip using default audio output
                    audioClip.open(audioStream);
                    audioClip.start();

                    while (!playCompleted) {
                        // wait for the playback completes
                        try {
                            Thread.sleep(500);
                        } catch (InterruptedException ex) {
                            ex.printStackTrace();
                            System.out.println("Send the above to a developer.");
                        }
                    }
                    // required catches
                } catch (UnsupportedAudioFileException aud) {
                    System.out.println("Audio file format is unsupported");
                } catch (IOException io) {
                    System.out.println("Audio file could not be found, give this error code to a developer: "
                            + io.getMessage());
                } catch (LineUnavailableException line) {
                    System.out.println("Audio clip could not be obtained");
                } catch (Exception ex) {
                    System.out.println("Unexpected error occured, please give this to a developer" + ex.getMessage());
                }
            }
        }).start();

    }

    @Override
    public void update(LineEvent event) {
        // this really isn't ever used, but it's required because we're implementing LineListener
        LineEvent.Type type = event.getType();

        if (type == LineEvent.Type.START) {
            // nothing to do
        } else if (type == LineEvent.Type.STOP) {
            playCompleted = true;
        }
    }
}
