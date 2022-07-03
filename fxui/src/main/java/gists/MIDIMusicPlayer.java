package gists;

import java.util.Arrays;
import java.util.List;

import javax.sound.midi.MidiChannel;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Synthesizer;

import javafx.animation.FillTransition;
import javafx.application.Application;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;

/**
 * 仿真 MIDI 音乐钢琴 
 * @author Almas Baimagambetov 
 * 
 */
public class MIDIMusicPlayer extends Application {


    private List<Note> notes = Arrays.asList(
            new Note("C", KeyCode.A, 60),
            new Note("D", KeyCode.S, 62),
            new Note("E", KeyCode.D, 64),
            new Note("F", KeyCode.F, 65),
            new Note("G", KeyCode.G, 67),
            new Note("A", KeyCode.H, 69),
            new Note("B", KeyCode.J, 71),
            new Note("C", KeyCode.K, 72),
            new Note("D", KeyCode.L, 74),
            new Note("E", KeyCode.SEMICOLON, 76)
    );

    private HBox root = new HBox(15);
    private MidiChannel channel;

    private Parent createContent() {
        this.loadChannel();

        this.root.setPrefSize(600, 150);

        this.notes.forEach(note -> {
            NoteView view = new NoteView(note);
            this.root.getChildren().add(view);
        });

        return root;
    }

    private void loadChannel() {
        try {
            Synthesizer synth = MidiSystem.getSynthesizer();
            synth.open();
            synth.loadInstrument(synth.getDefaultSoundbank().getInstruments()[5]);

            this.channel = synth.getChannels()[0];

        } catch (MidiUnavailableException e) {
            System.out.println("Cannot get synth");
            e.printStackTrace();
        }
    }

    @Override
    public void start(Stage stage) throws Exception {
        Scene scene = new Scene(createContent());
        scene.setOnKeyPressed(e -> onKeyPress(e.getCode()));

        stage.setScene(scene);
        stage.show();
    }

    private void onKeyPress(KeyCode key) {
        root.getChildren()
            .stream()
            .map(view -> (NoteView) view)
            .filter(view -> view.note.key.equals(key))
            .forEach(view -> {

                FillTransition ft = new FillTransition(
                        Duration.seconds(0.3),
                        view.bg,
                        Color.WHITE,
                        Color.BLACK
                );
                ft.setCycleCount(2);
                ft.setAutoReverse(true);
                ft.play();

                this.channel.noteOn(view.note.number, 90);
            });
    }

    private static class NoteView extends StackPane {
        public Note note;
        public Rectangle bg = new Rectangle(50, 150, Color.WHITE);

        public NoteView(Note note) {
            this.note = note;
            
            bg.setStroke(Color.BLACK);
            bg.setStrokeWidth(2.5);
            
            this.getChildren().addAll(bg, new Text(note.name));
        }
    }

    private static class Note {
        public String name;
        public KeyCode key;
        public Integer number;

        Note(String name, KeyCode key, Integer number) {
            this.name = name;
            this.key = key;
            this.number = number;
        }
    }

    public static void main(String[] args) {
        Application.launch(args);
    }
    
    
}








