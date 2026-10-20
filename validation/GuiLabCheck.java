import api.GradeDataBase;
import entity.Grade;
import entity.Team;
import usecase.*;
import javax.swing.*;
import java.awt.*;
import java.lang.reflect.Method;
import java.util.concurrent.atomic.AtomicReference;
import static org.mockito.Mockito.*;

/** Clicks the real Swing buttons with a synthetic database, without a live token. */
public class GuiLabCheck {
    public static void main(String[] args) throws Exception {
        GradeDataBase db = mock(GradeDataBase.class);
        when(db.getMyTeam()).thenReturn(new Team("synthetic", new String[]{"a", "b"}));
        when(db.getGrades("a")).thenReturn(new Grade[]{new Grade("a", "207", 81)});
        when(db.getGrades("b")).thenReturn(new Grade[]{new Grade("b", "207", 85)});
        AtomicReference<Throwable> failure = new AtomicReference<>();
        SwingUtilities.invokeAndWait(() -> {
            try {
                Method factory = app.gui.Application.class.getDeclaredMethod("createManageTeamCard", JFrame.class,
                    LeaveTeamUseCase.class, GetAverageGradeUseCase.class, GetTopGradeUseCase.class);
                factory.setAccessible(true);
                JPanel panel = (JPanel) factory.invoke(null, null, new LeaveTeamUseCase(db),
                    new GetAverageGradeUseCase(db), new GetTopGradeUseCase(db));
                JTextField field = null;
                for (Component c : panel.getComponents()) if (c instanceof JTextField) field = (JTextField)c;
                if (field == null) throw new AssertionError("No course input");
                for (String[] check : new String[][]{{"Get Average Grade", "Average Grade: 83.0"}, {"Get Top Grade", "Top Grade: 85.0"}}) {
                    JButton button = null;
                    for (Component c : panel.getComponents())
                        if (c instanceof JButton && ((JButton)c).getText().equals(check[0])) button = (JButton)c;
                    if (button == null) throw new AssertionError("Missing button: " + check[0]);
                    field.setText("207");
                    AtomicReference<String> message = new AtomicReference<>();
                    Timer timer = new Timer(150, event -> {
                        for (Window w : Window.getWindows()) if (w instanceof JDialog && w.isShowing()) {
                            for (Component c : ((JDialog)w).getContentPane().getComponents())
                                if (c instanceof JOptionPane) message.set(String.valueOf(((JOptionPane)c).getMessage()));
                            w.dispose();
                        }
                    });
                    timer.start(); button.doClick(); timer.stop();
                    if (!check[1].equals(message.get())) throw new AssertionError("Unexpected dialog: " + message.get());
                    System.out.println("PASS: real Swing " + check[0] + " -> " + message.get());
                }
            } catch (Throwable t) { failure.set(t); }
        });
        if (failure.get() != null) throw new AssertionError(failure.get());
        System.exit(0);
    }
}
