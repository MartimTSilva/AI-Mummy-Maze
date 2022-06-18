package showSolution;

import agent.Heuristic;
import agent.Solution;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.NoSuchElementException;
import javax.swing.*;

import mummyMaze.MummyMazeAgent;
import mummyMaze.MummyMazeProblem;
import mummyMaze.MummyMazeState;
import searchmethods.BeamSearch;
import searchmethods.DepthLimitedSearch;
import searchmethods.SearchMethod;

public class MainFrame extends JFrame {
    private MummyMazeAgent agent = new MummyMazeAgent(new MummyMazeState(null, null, null, null, null));
    private JComboBox comboBoxSearchMethods;
    private JComboBox comboBoxHeuristics;
    private JLabel labelSearchParameter = new JLabel("limit/beam size:");
    private JTextField textFieldSearchParameter = new JTextField("0", 5);
    private GameArea gameArea;
    private JButton buttonInitialState = new JButton("Read initial state");
    private JButton buttonSolve = new JButton("Solve");
    private JButton buttonStop = new JButton("Stop");
    private JButton buttonShowSolution = new JButton("Show solution");
    private JButton buttonReset = new JButton("Reset to initial state");
    private final JButton buttonLevelTest = new JButton("Test level");
    private JTextArea textArea;

    private StringBuilder testBuilder;
    private File testFile;
    private String selectedFileName;

    private final String FILE_HEADER = "Level;Search Algorithm;Heuristic;Limit Size;Solution Found;Solution Cost;Num of Expanded Nodes;Max Frontier Size;Num of Generated States\n";

    public MainFrame() {
        try {
            jbInit();
        } catch (Exception e) {
            e.printStackTrace(System.err);
        }
    }

    private void jbInit() throws Exception {
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setTitle("Mummy Maze");

        JPanel contentPane = (JPanel) this.getContentPane();
        contentPane.setLayout(new BorderLayout());
        JPanel panelButtons = new JPanel(new FlowLayout());
        panelButtons.add(buttonInitialState);
        buttonInitialState.addActionListener(new ButtonInitialState_ActionAdapter(this));
        panelButtons.add(buttonSolve);
        buttonSolve.setEnabled(false);
        buttonSolve.addActionListener(new ButtonSolve_ActionAdapter(this));
        panelButtons.add(buttonStop);
        buttonStop.setEnabled(false);
        buttonStop.addActionListener(new ButtonStop_ActionAdapter(this));
        panelButtons.add(buttonShowSolution);
        buttonShowSolution.setEnabled(false);
        buttonShowSolution.addActionListener(new ButtonShowSolution_ActionAdapter(this));
        panelButtons.add(buttonReset);
        buttonReset.setEnabled(false);
        buttonReset.addActionListener(new ButtonReset_ActionAdapter(this));
        panelButtons.add(buttonLevelTest);
        buttonLevelTest.setEnabled(false);
        buttonLevelTest.addActionListener(new ButtonLevelTest_ActionAdapter(this));

        JPanel panelSearchMethods = new JPanel(new FlowLayout());
        comboBoxSearchMethods = new JComboBox(agent.getSearchMethodsArray());
        panelSearchMethods.add(comboBoxSearchMethods);
        comboBoxSearchMethods.addActionListener(new ComboBoxSearchMethods_ActionAdapter(this));
        panelSearchMethods.add(labelSearchParameter);
        labelSearchParameter.setEnabled(false);
        panelSearchMethods.add(textFieldSearchParameter);
        textFieldSearchParameter.setEnabled(false);
        textFieldSearchParameter.setHorizontalAlignment(JTextField.RIGHT);
        textFieldSearchParameter.addKeyListener(new TextFieldSearchParameter_KeyAdapter(this));
        comboBoxHeuristics = new JComboBox(agent.getHeuristicsArray());
        panelSearchMethods.add(comboBoxHeuristics);
        comboBoxHeuristics.setEnabled(false);
        comboBoxHeuristics.addActionListener(new ComboBoxHeuristics_ActionAdapter(this));

        JPanel gamePanel = new JPanel(new FlowLayout());
        gameArea = new GameArea();
        gamePanel.add(gameArea);
        textArea = new JTextArea(15, 31);
        JScrollPane scrollPane = new JScrollPane(textArea);
        textArea.setEditable(false);
        gamePanel.add(scrollPane);

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.add(panelButtons, BorderLayout.NORTH);
        mainPanel.add(panelSearchMethods, BorderLayout.CENTER);
        mainPanel.add(gamePanel, BorderLayout.SOUTH);
        contentPane.add(mainPanel);

        Path testsFolder = Path.of("./tests");
        if (!Files.exists(testsFolder))
            Files.createDirectory(testsFolder);

        pack();
    }

    public void buttonInitialState_ActionPerformed(ActionEvent e) {
        JFileChooser fc = new JFileChooser(new java.io.File("./levels"));
        try {
            if (fc.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
                selectedFileName = fc.getSelectedFile().getName();
                gameArea.setState(agent.readInitialStateFromFile(fc.getSelectedFile()));
                buttonSolve.setEnabled(true);
                buttonShowSolution.setEnabled(false);
                buttonReset.setEnabled(false);
                buttonLevelTest.setEnabled(true);
            }
        } catch (IOException e1) {
            e1.printStackTrace(System.err);
        } catch (NoSuchElementException e2) {
            JOptionPane.showMessageDialog(this, "File format not valid", "Error!", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void comboBoxSearchMethods_ActionPerformed(ActionEvent e) {
        int index = comboBoxSearchMethods.getSelectedIndex();
        agent.setSearchMethod((SearchMethod) comboBoxSearchMethods.getItemAt(index));
        //puzzleTableModel.setPuzzle(agent.resetEnvironment());
        buttonSolve.setEnabled(true);
        buttonShowSolution.setEnabled(false);
        buttonReset.setEnabled(false);
        textArea.setText("");
        comboBoxHeuristics.setEnabled(index > 4); //Informed serch methods
        textFieldSearchParameter.setEnabled(index == 3 || index == 7); // limited depth or beam search
        labelSearchParameter.setEnabled(index == 3 || index == 7); // limited depth or beam search
    }

    public void comboBoxHeuristics_ActionPerformed(ActionEvent e) {
        int index = comboBoxHeuristics.getSelectedIndex();
        agent.setHeuristic((Heuristic) comboBoxHeuristics.getItemAt(index));
        gameArea.setState(agent.resetEnvironment());
        buttonSolve.setEnabled(true);
        buttonShowSolution.setEnabled(false);
        buttonReset.setEnabled(false);
        textArea.setText("");
    }

    public void buttonSolve_ActionPerformed(ActionEvent e) {
        SwingWorker worker = new SwingWorker<Solution, Void>() {
            @Override
            public Solution doInBackground() {
                textArea.setText("");
                buttonStop.setEnabled(true);
                buttonSolve.setEnabled(false);
                buttonInitialState.setEnabled(false);
                try {
                    prepareSearchAlgorithm();
                    MummyMazeProblem problem = new MummyMazeProblem(agent.getEnvironment().clone());
                    agent.solveProblem(problem);
                } catch (Exception e) {
                    e.printStackTrace(System.err);
                }
                return null;
            }

            @Override
            public void done() {
                if (!agent.hasBeenStopped()) {
                    textArea.setText(agent.getSearchReport());
                    if (agent.hasSolution()) {
                        buttonShowSolution.setEnabled(true);
                    }
                }
                buttonSolve.setEnabled(true);
                buttonStop.setEnabled(false);
                buttonInitialState.setEnabled(true);
            }
        };

        worker.execute();
    }

    public void buttonStop_ActionPerformed(ActionEvent e) {
        agent.stop();
        buttonShowSolution.setEnabled(false);
        buttonStop.setEnabled(false);
        buttonSolve.setEnabled(true);
    }

    public void buttonShowSolution_ActionPerformed(ActionEvent e) {
        buttonShowSolution.setEnabled(false);
        buttonStop.setEnabled(false);
        buttonSolve.setEnabled(false);
        buttonInitialState.setEnabled(false);
        SwingWorker worker = new SwingWorker<Void, Void>() {
            @Override
            public Void doInBackground() {
                agent.executeSolution();
                buttonReset.setEnabled(true);
                return null;
            }

            @Override
            public void done() {
                buttonShowSolution.setEnabled(false);
                buttonSolve.setEnabled(false);
                buttonInitialState.setEnabled(true);
            }
        };
        worker.execute();
    }

    public void buttonReset_ActionPerformed(ActionEvent e) {
        gameArea.setState(agent.resetEnvironment());
        buttonShowSolution.setEnabled(true);
        buttonReset.setEnabled(false);
    }

    public void buttonLevelTest_ActionPerformed() throws IOException {
        String dateStr = new SimpleDateFormat("dd-MM-yyyy (HH_mm_ms)").format(new Date());
        String testFilePath = "./tests/" + selectedFileName + "-" + dateStr + ".csv";
        testFile = new File(testFilePath);
        testBuilder = new StringBuilder();

        try {
            prepareForTest();
            SwingWorker worker = new SwingWorker<Void, Void>() {
                @Override
                public Void doInBackground() {
                    try {
                        textArea.setText("Testing " + selectedFileName + "...");
                        Files.writeString(testFile.toPath(), FILE_HEADER, StandardOpenOption.CREATE);
                        testLevel();
                    } catch (Exception e) {
                        e.printStackTrace(System.err);
                    }
                    return null;
                }

                @Override
                public void done() {
                    if (testBuilder.length() > 0) {
                        try {
                            Files.writeString(testFile.toPath(), testBuilder, StandardOpenOption.APPEND);
                        } catch (IOException e) {
                            e.printStackTrace(System.err);
                        }
                    }

                    buttonInitialState.setEnabled(true);
                    buttonSolve.setEnabled(true);
                    buttonStop.setEnabled(false);
                    comboBoxSearchMethods.setEnabled(true);
                    comboBoxHeuristics.setEnabled(true);
                    buttonLevelTest.setEnabled(true);
                }
            };
            worker.execute();


        } catch (NoSuchElementException e2) {
            JOptionPane.showMessageDialog(this, "File format not valid", "Error!", JOptionPane.ERROR_MESSAGE);
        }
    }

    private synchronized void testLevel() {
        for (int i = 0; i < agent.getSearchMethodsArray().length; i++) {
            //Don't test Beam and Depth Limited Search (i == 3 -> DepthLimitedSearch | i == 7 -> BeamSearch)
            if (i == 3 || i == 7)
                continue;

            SearchMethod searchMethod = agent.getSearchMethodsArray()[i];

            //Don't need heuristic if search method is not informed
            if (i > 4) {
                for (Heuristic heuristic : agent.getHeuristicsArray()) {
                    runSearchMethodTest(searchMethod, heuristic);
                }
            } else {
                runSearchMethodTest(searchMethod, null);
            }
        }
        textArea.append("\nTests completed!");
    }

    private void runSearchMethodTest(SearchMethod searchMethod, Heuristic heuristic) {
        textArea.append("\n\t" + searchMethod);

        if (heuristic != null)
            textArea.append(" (" + heuristic + ")");

        agent.setHeuristic(heuristic);
        agent.resetEnvironment();
        agent.setSearchMethod(searchMethod);

        textArea.append(" - ");
        MummyMazeProblem problem = new MummyMazeProblem(agent.getEnvironment().clone());
        testBuilder.append("\n").append(selectedFileName).append(";");
        try {
            agent.solveProblem(problem);
            if (agent.hasBeenStopped())
                textArea.append(" Stopped :(");
            else
                textArea.append(" OK!");
            testBuilder.append(agent.getCsvSearchReport());
        } catch (Exception e) {
            textArea.append("Something went wrong..\n ERROR: " + e.getMessage());
        }
    }

    private void prepareForTest() {
        textArea.setText("");
        buttonShowSolution.setEnabled(false);
        buttonReset.setEnabled(false);
        buttonInitialState.setEnabled(false);
        buttonSolve.setEnabled(false);
        buttonStop.setEnabled(true);
        comboBoxHeuristics.setEnabled(false);
        comboBoxSearchMethods.setEnabled(false);
        buttonLevelTest.setEnabled(false);

        testBuilder = new StringBuilder();
    }

    private void prepareSearchAlgorithm() {
        if (agent.getSearchMethod() instanceof DepthLimitedSearch) {
            DepthLimitedSearch searchMethod = (DepthLimitedSearch) agent.getSearchMethod();
            searchMethod.setLimit(Integer.parseInt(textFieldSearchParameter.getText()));
        } else if (agent.getSearchMethod() instanceof BeamSearch) {
            BeamSearch searchMethod = (BeamSearch) agent.getSearchMethod();
            searchMethod.setBeamSize(Integer.parseInt(textFieldSearchParameter.getText()));
        }
    }
}

class ComboBoxSearchMethods_ActionAdapter implements ActionListener {

    private final MainFrame adaptee;

    ComboBoxSearchMethods_ActionAdapter(MainFrame adaptee) {
        this.adaptee = adaptee;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        adaptee.comboBoxSearchMethods_ActionPerformed(e);
    }
}

class ComboBoxHeuristics_ActionAdapter implements ActionListener {

    private final MainFrame adaptee;

    ComboBoxHeuristics_ActionAdapter(MainFrame adaptee) {
        this.adaptee = adaptee;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        adaptee.comboBoxHeuristics_ActionPerformed(e);
    }
}

class ButtonInitialState_ActionAdapter implements ActionListener {

    private final MainFrame adaptee;

    ButtonInitialState_ActionAdapter(MainFrame adaptee) {
        this.adaptee = adaptee;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        adaptee.buttonInitialState_ActionPerformed(e);
    }
}

class ButtonSolve_ActionAdapter implements ActionListener {

    private final MainFrame adaptee;

    ButtonSolve_ActionAdapter(MainFrame adaptee) {
        this.adaptee = adaptee;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        adaptee.buttonSolve_ActionPerformed(e);
    }
}

class ButtonStop_ActionAdapter implements ActionListener {

    private final MainFrame adaptee;

    ButtonStop_ActionAdapter(MainFrame adaptee) {
        this.adaptee = adaptee;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        adaptee.buttonStop_ActionPerformed(e);
    }
}

class ButtonShowSolution_ActionAdapter implements ActionListener {

    private final MainFrame adaptee;

    ButtonShowSolution_ActionAdapter(MainFrame adaptee) {
        this.adaptee = adaptee;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        adaptee.buttonShowSolution_ActionPerformed(e);
    }
}

class ButtonReset_ActionAdapter implements ActionListener {

    private final MainFrame adaptee;

    ButtonReset_ActionAdapter(MainFrame adaptee) {
        this.adaptee = adaptee;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        adaptee.buttonReset_ActionPerformed(e);
    }
}

class ButtonLevelTest_ActionAdapter implements ActionListener {

    private final MainFrame adaptee;

    ButtonLevelTest_ActionAdapter(MainFrame adaptee) {
        this.adaptee = adaptee;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        try {
            adaptee.buttonLevelTest_ActionPerformed();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}

class TextFieldSearchParameter_KeyAdapter implements KeyListener {

    private final MainFrame adaptee;

    TextFieldSearchParameter_KeyAdapter(MainFrame adaptee) {
        this.adaptee = adaptee;
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {
    }

    @Override
    public void keyTyped(KeyEvent e) {
        char c = e.getKeyChar();
        if (!Character.isDigit(c) || c == KeyEvent.VK_BACK_SPACE || c == KeyEvent.VK_DELETE) {
            e.consume();
        }
    }
}
