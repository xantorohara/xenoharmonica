package io.github.xantorohara.xenoharmonica;

import io.github.xantorohara.xenoharmonica.midi.XenoMidiPlayer;
import io.github.xantorohara.xenoharmonica.midi.XenoMidiSynthesizer;

import javax.sound.midi.Instrument;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class XenoMenu extends JMenuBar {
    private Component owner;
    private InstrumentEmulator emulator;
    private XenoMidiSynthesizer synthesizer;
    private KeyboardListener keyboardListener;
    private Config config;

    private ArrayList<JRadioButtonMenuItem> instrumentsMenuItems = new ArrayList<JRadioButtonMenuItem>();
    private Set<ActionListener> xenoListeners = new HashSet<ActionListener>();

    public XenoMenu(Component owner, InstrumentEmulator emulator, XenoMidiSynthesizer synthesizer, KeyboardListener keyboardListener, Config config) {
        this.owner = owner;
        this.config = config;
        this.emulator = emulator;
        this.synthesizer = synthesizer;
        this.keyboardListener = keyboardListener;

        createFileMenu();
        createInstrumentsMenu();
        createKeyboardMenu();
        createHelpMenu();
    }

    public void addXenoListener(ActionListener listener) {
        xenoListeners.add(listener);
    }

    private void notifyXenoListeners() {
        ActionEvent event = new ActionEvent(this, 0, null);
        for (ActionListener listener : xenoListeners) {
            listener.actionPerformed(event);
        }
    }

    private class InstrumentChangeAction extends AbstractAction {
        private int instrumentNo;

        public InstrumentChangeAction(String instrumentName, int instrumentNo) {
            super(instrumentName);
            this.instrumentNo = instrumentNo;
        }

        public void actionPerformed(ActionEvent e) {
            synthesizer.setSelecteInstrumentNumber(instrumentNo);
            config.setInstrument(synthesizer.getSelecteInstrumentNumber());
            notifyXenoListeners();
        }
    }

    private void updateInstruments() {
        Instrument[] allInstruments = synthesizer.getInstruments();
        int[] cnfInstruments = config.getInstruments();
        for (int i = 0; i < cnfInstruments.length && i < instrumentsMenuItems.size(); i++) {
            int instrNo = cnfInstruments[i];
            if (instrNo >= 0 && instrNo < allInstruments.length) {
                Instrument instrument = allInstruments[instrNo];
                JRadioButtonMenuItem item = instrumentsMenuItems.get(i);
                item.setAction(new InstrumentChangeAction(instrument.getName(), instrNo));
                item.setSelected(instrNo == config.getInstrument());
                if (i < 10)
                    item.setAccelerator(KeyStroke.getKeyStroke(Character.forDigit((i + 1) % 10, 10)));
            }
        }
    }

    private void createInstrumentsMenu() {
        JMenu menu = new JMenu("Instruments");
        ButtonGroup instrumentButtonGroup = new ButtonGroup();
        int[] cnfInstruments = config.getInstruments();

        if (synthesizer.getInstruments().length > 0) {
            for (int i = 0; i < cnfInstruments.length; i++) {
                JRadioButtonMenuItem menuItem = new JRadioButtonMenuItem();
                instrumentButtonGroup.add(menuItem);
                menu.add(menuItem);
                instrumentsMenuItems.add(menuItem);
            }
        }
        updateInstruments();
        menu.add(new JSeparator());

        JMenuItem menuInstrumentsSetup = new JMenuItem(new AbstractAction("Setup ...") {
            public void actionPerformed(ActionEvent e) {
                int[] instruments = config.getInstruments().clone();
                int instrument = synthesizer.getSelecteInstrumentNumber();
                XenoSetupPanel xenoSetupPanel = new XenoSetupPanel(synthesizer, instruments, keyboardListener);
                int result = JOptionPane.showOptionDialog(owner, new Object[]{xenoSetupPanel}, "Instruments setup", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE, null, null, null);
                if (result == JOptionPane.OK_OPTION) {
                    config.setInstruments(xenoSetupPanel.getSelectedInstrumentNumbers());
                    updateInstruments();
                } else {
                    synthesizer.setSelecteInstrumentNumber(instrument);
                }
                emulator.getUserInterface().requestFocus();
                notifyXenoListeners();
            }
        });

        menu.add(menuInstrumentsSetup);
        add(menu);
    }

    private void createKeyboardMenu() {
        JMenu menu = new JMenu("Keyboard");
        JMenuItem menuKeyboardIncr = new JMenuItem(new AbstractAction("Octave +") {
            public void actionPerformed(ActionEvent e) {
                synthesizer.stopAllNotes();
                emulator.setOctave(emulator.getOctave() + 1);
                config.setOctave(emulator.getOctave());
                notifyXenoListeners();
            }
        });
        menuKeyboardIncr.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_EQUALS, KeyEvent.VK_UNDEFINED));
        menu.add(menuKeyboardIncr);

        JMenuItem menuKeyboardDecr = new JMenuItem(new AbstractAction("Octave -") {
            public void actionPerformed(ActionEvent e) {
                synthesizer.stopAllNotes();
                emulator.setOctave(emulator.getOctave() - 1);
                config.setOctave(emulator.getOctave());
                notifyXenoListeners();
            }
        });
        menuKeyboardDecr.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_MINUS, KeyEvent.VK_UNDEFINED));
        menu.add(menuKeyboardDecr);

        menu.add(new JSeparator());

        JCheckBoxMenuItem menuKeyboardNotes = new JCheckBoxMenuItem(new AbstractAction("Show notes") {
            public void actionPerformed(ActionEvent e) {
                boolean isSelected = ((JCheckBoxMenuItem) e.getSource()).isSelected();
                emulator.setShowNotes(((JCheckBoxMenuItem) e.getSource()).isSelected());
                config.setShowNotes(isSelected);
            }
        });
        menuKeyboardNotes.setSelected(config.isShowNotes());
        menu.add(menuKeyboardNotes);

        JCheckBoxMenuItem menuKeyboardLayout = new JCheckBoxMenuItem(new AbstractAction("Show keyboard layout") {
            public void actionPerformed(ActionEvent e) {
                boolean isSelected = ((JCheckBoxMenuItem) e.getSource()).isSelected();
                emulator.setShowLayout(isSelected);
                config.setShowLayout(isSelected);
            }
        });
        menuKeyboardLayout.setSelected(config.isShowLayout());
        menu.add(menuKeyboardLayout);

        add(menu);
    }

    private void createFileMenu() {
        if (owner instanceof Window) {
            JMenu menuFile = new JMenu("File");
            JMenuItem menuFileExit = new JMenuItem(new AbstractAction("Exit") {
                public void actionPerformed(ActionEvent e) {
                    ((Window) owner).dispose();
                }
            });
            menuFile.add(menuFileExit);
            add(menuFile);
        }
    }

    private void createHelpMenu() {
        JMenu menuHelp = new JMenu("Help");

        JMenuItem menuHelpAbout = new JMenuItem(new AbstractAction("About") {
            public void actionPerformed(ActionEvent e) {
                InputStream is = XenoMenu.this.getClass().getResourceAsStream("resources/about.mid");
                XenoMidiPlayer player = null;
                try {
                    player = new XenoMidiPlayer();
                    player.play(is);
                } catch (Exception exc) {
                    exc.printStackTrace();
                }
                JOptionPane.showMessageDialog(XenoMenu.this, new AboutPanel(), "About", JOptionPane.PLAIN_MESSAGE);
                if (player != null)
                    player.stop();
                emulator.getUserInterface().requestFocus();
            }
        });
        menuHelp.add(menuHelpAbout);
        add(menuHelp);
    }
}
