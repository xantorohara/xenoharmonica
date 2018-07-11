package io.github.xantorohara.xenoharmonica;

import io.github.xantorohara.xenoharmonica.midi.XenoMidiSynthesizer;

import javax.sound.midi.Instrument;
import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableModel;
import java.awt.*;

public class XenoSetupPanel extends JPanel {

    private Instrument[] selectedInstruments;
    private XenoMidiSynthesizer synthesizer;

    public int[] getSelectedInstrumentNumbers() {
        int[] result = new int[selectedInstruments.length];
        Instrument[] allInstruments = synthesizer.getInstruments();
        for (int i = 0; i < selectedInstruments.length; i++) {
            for (int j = 0; j < allInstruments.length; j++) {
                if (selectedInstruments[i] == allInstruments[j]) {
                    result[i] = j;
                    break;
                }
            }
        }
        return result;
    }

    public XenoSetupPanel(XenoMidiSynthesizer synthesizer, int[] selectedInstrumentNumbers, KeyboardListener listener) {
        this.synthesizer = synthesizer;

        Instrument[] allInstruments = synthesizer.getInstruments();
        selectedInstruments = new Instrument[selectedInstrumentNumbers.length];

        for (int i = 0; i < selectedInstrumentNumbers.length && selectedInstrumentNumbers[i] < allInstruments.length; i++)
            selectedInstruments[i] = allInstruments[selectedInstrumentNumbers[i]];

        TableModel model = new IntrumentsTableModel(selectedInstruments);
        JTable table = new JTable();
        table.setAutoResizeMode(JTable.AUTO_RESIZE_NEXT_COLUMN);
        table.setModel(model);
        table.getColumnModel().getColumn(0).setPreferredWidth(40);

        InstrumentListModel instrumentListModel = new InstrumentListModel(synthesizer);
        JComboBox<Instrument> editor = new JComboBox<Instrument>(instrumentListModel);

        table.getColumnModel().getColumn(1).setCellEditor(new InstrumentTableCellEditor(editor));
        table.getColumnModel().getColumn(1).setCellRenderer(new InstrumentTableCellRenderer());
        table.getColumnModel().getColumn(1).setPreferredWidth(200);
        table.setRowHeight(20);

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setPreferredSize(new Dimension(240, 180));

        editor.setRenderer(new InstrumentListRenderer());
        editor.addKeyListener(listener);
        table.addKeyListener(listener);
        add(scrollPane);
    }

    private static class InstrumentTableCellEditor extends DefaultCellEditor {
        public InstrumentTableCellEditor(JComboBox comboBox) {
            super(comboBox);
            setClickCountToStart(2);
        }
    }

    public class InstrumentTableCellRenderer extends DefaultTableCellRenderer {
        protected void setValue(Object value) {
            if (value instanceof Instrument)
                super.setValue(((Instrument) value).getName());
            else
                super.setValue(value);
        }
    }

    private static class IntrumentsTableModel extends AbstractTableModel {
        private String[] header = new String[]{"", "Instrument"};
        private Instrument[] instruments;

        public IntrumentsTableModel(Instrument[] instruments) {
            this.instruments = instruments;
        }

        public String getColumnName(int column) {
            return header[column];
        }

        public int getRowCount() {
            return instruments.length;
        }

        public int getColumnCount() {
            return header.length;
        }

        public boolean isCellEditable(int rowIndex, int columnIndex) {
            return columnIndex == 1;
        }

        public Object getValueAt(int row, int column) {
            return column == 0 ? row + 1 : instruments[row];
        }

        public void setValueAt(Object value, int row, int column) {
            instruments[row] = (Instrument) value;
        }
    }

    private static class InstrumentListModel extends DefaultComboBoxModel<Instrument> {
        private XenoMidiSynthesizer synthesizer;
        private Instrument[] instruments;
        private Instrument selectedInstrument;

        public InstrumentListModel(XenoMidiSynthesizer synthesizer) {
            this.synthesizer = synthesizer;
            this.instruments = synthesizer.getInstruments();
        }

        public void setSelectedItem(Instrument anItem) {
            selectedInstrument = anItem;
            synthesizer.setSelecteInstrument(selectedInstrument);
        }

        public Instrument getSelectedItem() {
            return selectedInstrument;
        }

        public int getSize() {
            return instruments == null ? 0 : instruments.length;
        }

        public Instrument getElementAt(int index) {
            return instruments[index];
        }

    }

    private static class InstrumentListRenderer extends DefaultListCellRenderer {
        public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
            if (value instanceof Instrument)
                return super.getListCellRendererComponent(list, ((Instrument) value).getName(), index, isSelected, cellHasFocus);
            else
                return super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
        }
    }
}
