package com.optimizely.intellij.plugin.components;

import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.ui.ComboBox;
import com.intellij.openapi.ui.DialogWrapper;
import com.intellij.openapi.ui.Messages;
import com.intellij.ui.components.JBScrollPane;
import com.intellij.ui.table.JBTable;
import com.optimizely.ab.Optimizely;
import com.optimizely.intellij.plugin.service.OptimizelyFactoryService;
import com.optimizely.intellij.plugin.utils.LogAppender;
import com.optimizely.intellij.plugin.utils.OptimizelyUtil;

import javax.annotation.Nullable;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static javax.swing.BoxLayout.*;

public class OptDialogWrapper extends DialogWrapper {
    public OptDialogWrapper() {
        super(true); // use current window as parent
        init();
        setTitle("Test DialogWrapper");
    }

    @Nullable
    @Override
    protected JComponent createCenterPanel() {
        OptimizelyFactoryService factory = ServiceManager.getService(OptimizelyFactoryService.class);

        Optimizely optimizely = factory.getCurrentOptimizely();
        if (optimizely == null) {
            Messages.showErrorDialog("Please set your SDK Key first!", "Optimizely not initialized");
            return null;
        }

        JPanel dialogPanel = new JPanel();

        BoxLayout bl = new BoxLayout(dialogPanel, Y_AXIS);

        dialogPanel.setLayout(bl);

        // Set border for the panel
        dialogPanel.setBorder(new EmptyBorder(new Insets(150, 200, 150, 200)));
        //panel.setBorder(new EmptyBorder(new Insets(50, 80, 50, 80)));

        String[] methods = {"activate()", "getVariation()", "isFeatureEnabled()", "getFeatureVariableString()",
                "getFeatureVariableInteger()", "getFeatureVariableDouble()", "getFeatureVariableBoolean()", "track()"};

        ComboBox cb = new ComboBox(methods);

        final ComboBox noun = new ComboBox();

        final DefaultComboBoxModel[] model = {new DefaultComboBoxModel(optimizely.getProjectConfig().getExperimentKeyMapping().keySet().toArray())};
        noun.setModel(model[0]);

        ComboBox variations = new ComboBox();
        variations.setModel(new DefaultComboBoxModel());
        variations.setVisible(false);

        noun.addItemListener(event -> {
            if (event.getStateChange() != ItemEvent.SELECTED) { return; }
            String val = (String) event.getItem();
            String api = (String) cb.getSelectedItem();
            if (!OptimizelyUtil.isExperimentApi(api)
                    && OptimizelyUtil.isOptimizelyMethod(api)
                    && optimizely.getOptimizelyConfig().getFeaturesMap().get(val) != null) {
                variations.removeAllItems();
                DefaultComboBoxModel m = new
                        DefaultComboBoxModel(optimizely.getOptimizelyConfig().
                        getFeaturesMap().get(val).
                        getVariablesMap().keySet().toArray());
                variations.setModel(m);
                variations.setVisible(true);
            }
            else {
                variations.setVisible(false);
            }
        });

        Box b0 = Box.createHorizontalBox();

        JLabel label = new JLabel("This dialog uses the Optimizely Java SDK to test your configuration.\n" +
                "  The logging might be slightly different on your implementation.");

        b0.add(label);

        dialogPanel.add(b0);

        JLabel userLabel = new JLabel("UserID:");
        final JTextField userId = new JTextField(UUID.randomUUID().toString());

        Box b = Box.createHorizontalBox();
        b.add(userLabel);
        b.add(userId);
        dialogPanel.add(b);

        Box b2 = Box.createHorizontalBox();
        b2.add(cb);
        b2.add(noun);
        b2.add(variations);
        dialogPanel.add(b2);

        cb.addItemListener(event -> {
            if (event.getStateChange() != ItemEvent.SELECTED) return;
            String val = (String) event.getItem();
            if (OptimizelyUtil.isExperimentApi(val)) {
                noun.removeAllItems();
                model[0] = new DefaultComboBoxModel(optimizely.getProjectConfig().getExperimentKeyMapping().keySet().toArray());
                noun.setModel(model[0]);
            }
            else if (OptimizelyUtil.isOptimizelyMethod(val)) {
                noun.removeAllItems();
                model[0] = new DefaultComboBoxModel(optimizely.getProjectConfig().getFeatureKeyMapping().keySet().toArray());
                noun.setModel(model[0]);
            }
            else {
                noun.removeAllItems();
                model[0] = new DefaultComboBoxModel(optimizely.getProjectConfig().getEventNameMapping().keySet().toArray());
                noun.setModel(model[0]);
            }
            variations.setVisible(false);
        });

        JBTable table = getAttributeTable();

        final DefaultTableModel tableModel = (DefaultTableModel)table.getModel();

        Box buttons = Box.createVerticalBox();
        JButton add = new JButton("+");
        add.addActionListener(e -> {
            tableModel.addRow(new String[]{"", "", ""});
        });

        JButton remove = new JButton("-");
        remove.addActionListener(e -> {
            tableModel.removeRow(tableModel.getRowCount()-1);
        });
        buttons.add(add);
        buttons.add(remove);

        Box attrs = Box.createHorizontalBox();
        attrs.add(new JBScrollPane(table));
        attrs.add(buttons);

        dialogPanel.add(attrs);

        Box logging = Box.createHorizontalBox();

        JTextArea textArea = new JTextArea();

        //textArea.setFont(new Font("Serif", Font.ITALIC, 16));
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);

        JBScrollPane areaScrollPane = new JBScrollPane(textArea);
        areaScrollPane.setVerticalScrollBarPolicy(
                JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        areaScrollPane.setPreferredSize(new Dimension(250, 1000));

        logging.add(areaScrollPane);
        dialogPanel.add(logging);

        JLabel responseLabel = new JLabel("Response:");
        final JTextField responseField = new JTextField();
        responseField.setEditable(false);

        Box responseBox = Box.createHorizontalBox();
        responseBox.add(responseLabel);
        responseBox.add(responseField);
        dialogPanel.add(responseBox);


        JButton button = new JButton("Run");

        button.addActionListener(e -> {
            System.out.println("here");
            String id = userId.getText();
            String api = (String) cb.getSelectedItem();
            String key = (String) noun.getSelectedItem();
            String variation = variations.isVisible() ? (String) variations.getSelectedItem() : null;
            Object response = null;

            Map<String, ?> attributes = getAttributes(tableModel);

            LogAppender.captureLogging.set(true);

            switch (api) {
                case "activate()":
                    response = optimizely.activate(key, id, attributes);
                    break;
                case "getVariation()":
                    response = optimizely.getVariation(key, id, attributes);
                    break;
                case "isFeatureEnabled()":
                    response = optimizely.isFeatureEnabled(key, id, attributes);
                    break;
                case "getFeatureVariableString()":
                    response = optimizely.getFeatureVariableString(key, variation, id, attributes);
                    break;
                case "getFeatureVariableInteger()":
                    response = optimizely.getFeatureVariableInteger(key, variation, id, attributes);
                    break;
                case "getFeatureVariableDouble()":
                    response = optimizely.getFeatureVariableDouble(key, variation, id, attributes);
                    break;
                case "getFeatureVariableBoolean()":
                    response = optimizely.getFeatureVariableBoolean(key, variation, id, attributes);
                    break;
                case "track()":
                    optimizely.track(key, id, attributes);
                    break;
            }
            responseField.setText((response == null ? "null response" : response.toString()));

            LogAppender.captureLogging.set(false);
            textArea.setText("");

            for (int i = 0; i < LogAppender.logs.size(); i ++) {
                textArea.append(LogAppender.logs.get(i));
                textArea.append("\n");
            }

            LogAppender.clearLogs();
        });

        dialogPanel.add(button);

        return dialogPanel;
    }

    private Map<String,?> getAttributes(DefaultTableModel model) {
        HashMap<String, Object> map = new HashMap<>();
        for (int count = 0; count < model.getRowCount(); count++){
            String v = model.getValueAt(count, 0).toString();
            if (v == null || v.isEmpty()) continue;
            map.put(v, getValue(model, count));
        }

        return map;
    }

    private Object getValue(DefaultTableModel model, int count) {
        String value = model.getValueAt(count, 1).toString();
        String type = model.getValueAt(count, 2).toString();

        switch (type) {
            case "Double":
                return new Double(value);
            case "Integer":
                return new Integer(value);
            case "String":
                return value;
            case "Boolean":
                return new Boolean(value);
        }

        return null;
    }

    JBTable getAttributeTable() {
        OptimizelyFactoryService factory = ServiceManager.getService(OptimizelyFactoryService.class);

        Optimizely optimizely = factory.getCurrentOptimizely();
        if (optimizely == null) {
            Messages.showErrorDialog("Please set your SDK Key first!", "Optimizely not initialized");
            return null;
        }

        //headers for the table
        String[] columns = new String[] {
                "Attribute Name", "Value", "Type"
        };

        String[] types = new String[] {"String", "Double", "Boolean", "Integer"};

        //actual data for the table in a 2d array
        Object[][] data = new Object[][] {
                {"", "", ""}
        };

        final Class[] columnClass = new Class[] {
                String.class, String.class, String.class
        };
        //create table model with data
        DefaultTableModel model = new DefaultTableModel(data, columns) {
            @Override
            public boolean isCellEditable(int row, int column)
            {
                return true;
            }
            @Override
            public Class<?> getColumnClass(int columnIndex)
            {
                return columnClass[columnIndex];
            }
        };

        JBTable table = new JBTable(model);

        TableColumn attr = table.getColumnModel().getColumn(0);
        ComboBox comboBox = new ComboBox(optimizely.getProjectConfig().getAttributes().stream().map(a -> a.getKey()).toArray());
        attr.setCellEditor(new DefaultCellEditor(comboBox));

        attr = table.getColumnModel().getColumn(2);
        comboBox = new ComboBox(types);
        attr.setCellEditor(new DefaultCellEditor(comboBox));

        return table;
    }
}
