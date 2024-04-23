package pl.amrusb.util.ui.panels;
import javax.swing.JEditorPane;
import javax.swing.text.DefaultCaret;

public class CHtmlEditorPane extends JEditorPane {

    public CHtmlEditorPane(String html) {
        super();
        this.setContentType("text/html");
        this.setText(html);
        this.setEditable(false);
        this.setCaret(new DefaultCaret(){
            @Override
            public void setSelectionVisible(boolean visible) {
                super.setSelectionVisible(false);
            }
        });
    }
}