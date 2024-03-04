package pl.amrusb.util.ui.panels;
import javax.swing.JEditorPane;
import javax.swing.text.DefaultCaret;
import java.awt.event.MouseAdapter;

public class CHtmlEditorPane extends JEditorPane {

    public CHtmlEditorPane(String html) {
        super();
        this.setContentType("text/html");
        this.setText(html);
        this.setEditable(false);
        disableSelection();
        this.setCaret(new NonSelectableCaret());
    }


    private void disableSelection() {
        this.addMouseListener(new MouseAdapter() {});
        this.addMouseMotionListener(new MouseAdapter() {});
    }

    private class NonSelectableCaret extends DefaultCaret {

        public NonSelectableCaret() {
            setBlinkRate(0);
            this.setSize(0,0);
            this.setVisible(false);
        }

        @Override
        public void setSelectionVisible(boolean visible) {
            super.setSelectionVisible(false);
        }
    }
}