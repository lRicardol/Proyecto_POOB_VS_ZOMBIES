import domain.*;
import org.junit.Test;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

import static org.junit.Assert.*;

public class AcquireSunButtonTest {

    @Test
    public void testButtonInitialization() {
        AcquireSunButton acquireSunButton = new AcquireSunButton(
                800, 600, 25,
                () -> {}, // Mock de `updateSunPointsLabel`
                () -> {}  // Mock de `addSunPoints`
        );
        JButton button = acquireSunButton.getButton();

        assertNotNull("El botón no debería ser nulo.", button);
        assertEquals("El botón debería estar en la posición inicial X.", 775, button.getBounds().x);
        assertEquals("El botón debería estar en la posición inicial Y.", 10, button.getBounds().y);
        assertFalse("El botón debería ser transparente.", button.isOpaque());
        assertFalse("El botón no debería tener relleno.", button.isContentAreaFilled());
        assertFalse("El botón no debería mostrar bordes.", button.isBorderPainted());
    }

    @Test
    public void testButtonActionListener() {
        final boolean[] sunPointsUpdated = {false};
        final boolean[] labelUpdated = {false};

        AcquireSunButton acquireSunButton = new AcquireSunButton(
                800, 600, 25,
                () -> labelUpdated[0] = true,  // Simula `updateSunPointsLabel`
                () -> sunPointsUpdated[0] = true // Simula `addSunPoints`
        );
        JButton button = acquireSunButton.getButton();

        // Simular el clic en el botón
        for (ActionListener listener : button.getActionListeners()) {
            listener.actionPerformed(null);
        }

        assertTrue("Debería actualizar los puntos de sol.", sunPointsUpdated[0]);
        assertTrue("Debería actualizar la etiqueta de puntos de sol.", labelUpdated[0]);
        assertFalse("El botón debería ser invisible tras el clic.", button.isVisible());
    }

    @Test
    public void testSlideTimer() {
        AcquireSunButton acquireSunButton = new AcquireSunButton(
                800, 600, 25,
                () -> {}, // Mock de `updateSunPointsLabel`
                () -> {}  // Mock de `addSunPoints`
        );
        JButton button = acquireSunButton.getButton();

        Rectangle initialBounds = button.getBounds();
        try {
            // Espera 500ms para simular el avance del Timer
            Thread.sleep(500);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        Rectangle currentBounds = button.getBounds();
        assertTrue("El botón debería haberse movido hacia abajo.", currentBounds.y > initialBounds.y);
    }
}
