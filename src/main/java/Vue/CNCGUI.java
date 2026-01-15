package Vue;

import Domain.Controleur;
import Domain.DTO.*;

import com.intellij.uiDesigner.core.GridLayoutManager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.geom.Point2D;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.UUID;


public class CNCGUI extends JFrame {
    private Controleur controleur = new Controleur();
    private CNCDTO currentdto;

    private boolean isAddingCoupe = false;
    private boolean isAddingCoupeParallele = false;
    private boolean isAddingCoupeEnL = false;
    private boolean isAddingCoupeRectangulaire = false;

    private boolean MagnetiserlaGrille = false;

    private boolean isSelectingParalleleReference = false;

    private boolean isSelectingRectangulaireReference = false;
    private boolean isSelectingRectangulaireCoin1 = false;
    private boolean isSelectingRectangulaireCoin2 = false;

    private boolean isSelectingEnLCoin1 = false;
    private boolean isSelectingEnLCoin2 = false;

    private Point2D.Double initialClickPoint = null;
    private UUID draggedCoupeId = null;

    private List<Point2D.Double> ptsClique = new ArrayList<>();
    private UUID selectedElement = null;
    private UUID selectedZone = null;

    private boolean isEditingParallele = false;
    private boolean isEditingEnBordure = false;

    private boolean isSwitchingPanel = false;

    JList<String> outilListe;
    JList<String> zoneListe;

    // PANEL DYNAMIQUES
    JPanel panelParallele;
    JPanel panelRectangulaire;
    JPanel panelEnL;
    JPanel panelEnBordure;
    JPanel panelOutils;
    JPanel panelCoupes;
    JPanel panelZonesInterdites;
    JPanel panelPanneau;


    // Panel Parallèle

    JTextField paralleleTailleField;
    JTextField paralleleProfondeurField;
    JButton paralleleReferenceButton;
    JButton paralleleSupprimerButton;
    JComboBox<String> paralleleOutilComboBox;


    // Panel Rectangulaire

    JButton rectangulaireReferenceButton;
    JButton rectangulaireCoin1Button;
    JButton rectangulaireCoin2Button;
    JTextField rectangulaireReferenceXField;
    JTextField rectangulaireReferenceYField;
    JTextField rectangulaireCoin1XField;
    JTextField rectangulaireCoin1YField;
    JTextField rectangulaireCoin2XField;
    JTextField rectangulaireCoin2YField;
    JTextField rectangulaireProfondeurField;
    JButton rectangulaireSupprimerButton;
    JComboBox<String> rectangulaireOutilComboBox;


    // Panel En L

    JButton enlCoin1Button;
    JButton enlCoin2Button;
    JTextField enlCoin1XField;
    JTextField enlCoin1YField;
    JTextField enlCoin2XField;
    JTextField enlCoin2YField;
    JTextField enlProfondeurField;
    JButton enlSupprimerButton;
    JComboBox<String> enlOutilComboBox;

    // Panel En Bordure

    JTextField enbordureLargeurField;
    JTextField enbordureHauteurField;
    JTextField enbordureProfondeurField;
    JButton enbordureAppliquerButton;
    JButton enbordureSupprimerButton;
    JComboBox<String> enbordureOutilComboBox;

    // Panel Coupes

    // Panel Zones Interdites
    JButton zoneAjouterButton;
    JButton zoneSupprimerButton;

    JLabel x1Label;

    JLabel y1Label;

    JLabel x2Label;

    JLabel y2Label;

    JTextField zoneInterditeX1Field;
    JTextField zoneInterditeY1Field;
    JTextField zoneInterditeX2Field;
    JTextField zoneInterditeY2Field;


    JTextField zoneMmField;

    JTextField zoneLargeur;
    JTextField zoneHauteur;

    private JTextArea zoneConsoleTextArea;


    // Panel Panneau

    JTextField panneauLargeurField;
    JTextField panneauHauteurField;
    JTextField panneauEpaisseurField;
    JButton panneauAppliquerButton;

    // Panel Erreur

    JPanel erreurPanel;

    // Panel Outil

    JButton addOutil;
    JButton delOutil;
    JTextField outilNom;
    JTextField outilDiametre;

    private JTextArea consoleTextArea;

    // Drawing Panel

    private DrawingPanel drawingPanel;

    private JPanel dynamicLeftPanel;

    private JButton exporterButton = new JButton("Exporter");


    public CNCGUI() {
        JPanel staticRightPanel = new JPanel();
        staticRightPanel.setLayout(new BorderLayout());

        JPanel topSectionPanel = new JPanel();
        topSectionPanel.setLayout(new BoxLayout(topSectionPanel, BoxLayout.Y_AXIS));

        JPanel buttonPanelRadio = new JPanel(new GridLayout(1, 2, 5, 5));
        JButton coupeRectangulaireButton = new JButton("Ajouter une coupe Rectangulaire");
        JButton coupeEnLButton = new JButton("Ajouter une coupe en L");
        JButton coupeParallelButton = new JButton("Ajouter une coupe Parallèle");
        JButton coupeEnBordureButton = new JButton("Ajouter une coupe en Bordure");
        ButtonGroup group = new ButtonGroup();
        group.add(coupeEnLButton);
        group.add(coupeRectangulaireButton);
        group.add(coupeParallelButton);
        group.add(coupeEnBordureButton);

        buttonPanelRadio.add(coupeRectangulaireButton);
        buttonPanelRadio.add(coupeEnLButton);
        buttonPanelRadio.add(coupeParallelButton);
        buttonPanelRadio.add(coupeEnBordureButton);

        JPanel buttonPanelOther = new JPanel(new GridLayout(1, 4, 5, 5));
        JButton outilButton = new JButton("Gérer les outils");

        JButton zoneInterditeButton = new JButton("Gérer les zones interdites");
        JButton Grille = new JButton("Gérer la Grille");
        JButton panneauButton = new JButton("Gérer le panneau");
        buttonPanelOther.add(panneauButton);
        buttonPanelOther.add(outilButton);
        buttonPanelOther.add(Grille);
        buttonPanelOther.add(zoneInterditeButton);


        topSectionPanel.add(buttonPanelRadio);
        topSectionPanel.add(buttonPanelOther);

        this.drawingPanel = new DrawingPanel();
        drawingPanel.setControleur(controleur);
        //controleur.addPanneau(914.4, 914.4, 1);
        refreshAll();


        JPanel middleSectionPanel = new JPanel(new BorderLayout());
        middleSectionPanel.add(drawingPanel, BorderLayout.CENTER);


        staticRightPanel.add(topSectionPanel, BorderLayout.NORTH);
        staticRightPanel.add(middleSectionPanel, BorderLayout.CENTER);


        dynamicLeftPanel = new JPanel();
        dynamicLeftPanel.setLayout(new BorderLayout());

        JPanel northPanel = new JPanel(new GridLayout(1, 2, 5, 5));
        JButton undoButton = new JButton("↩");
        undoButton.addActionListener(e -> {
            if (controleur.undo()) {
                refreshAll();
                addToConsole("Undid last action.", Color.GREEN);
            } else {
                addToConsole("No action to undo.", Color.RED);
            }
        });
        JButton redoButton = new JButton("↪");
        redoButton.addActionListener(e -> {
            if (controleur.redo()) {
                refreshAll();
                addToConsole("Redid last action.", Color.GREEN);
            } else {
                addToConsole("No action to redo.", Color.RED);
            }
        });
        northPanel.add(undoButton);
        northPanel.add(redoButton);

        JPanel southPanel = new JPanel();
        southPanel.setLayout(new BorderLayout());
        JButton resetZoomButton = new JButton("Réinitialiser le zoom");
        resetZoomButton.addActionListener(e -> {
            drawingPanel.resetZoom();
        });

        exporterButton.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setDialogTitle("Exporter le fichier GCODE");

            FileNameExtensionFilter gcodeFilter = new FileNameExtensionFilter(
                    "GCODE files", "gcode"
            );
            fileChooser.setFileFilter(gcodeFilter);

            int userSelection = fileChooser.showSaveDialog(null);

            if (userSelection == JFileChooser.APPROVE_OPTION) {
                File fileToSave = fileChooser.getSelectedFile();

                if (!fileToSave.getName().toLowerCase().endsWith(".gcode")) {
                    fileToSave = new File(fileToSave.getAbsolutePath() + ".gcode");
                }

                System.out.println("Chemin du fichier gcode : " + fileToSave.getAbsolutePath());

                try {
                    controleur.convertToGCODE(fileToSave.getAbsolutePath());
                } catch (IOException ex) {
                    addToConsole(ex.getMessage(), Color.RED);
                }
            }
        });
        JButton sauvegarderButton = new JButton("Sauvegarder");
        sauvegarderButton.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setDialogTitle("Sauvegarder le fichier CNC");

            FileNameExtensionFilter cncFilter = new FileNameExtensionFilter(
                    "CNC Files", "cnc"
            );
            fileChooser.setFileFilter(cncFilter);

            int userSelection = fileChooser.showSaveDialog(null);

            if (userSelection == JFileChooser.APPROVE_OPTION) {
                File fileToSave = fileChooser.getSelectedFile();

                if (!fileToSave.getName().toLowerCase().endsWith(".cnc")) {
                    fileToSave = new File(fileToSave.getAbsolutePath() + ".cnc");
                }

                System.out.println("Chemin du fichier à sauvegarder : " + fileToSave.getAbsolutePath());

                try {
                    controleur.save(fileToSave.getAbsolutePath());
                } catch (IOException ex) {
                    addToConsole(ex.getMessage(), Color.RED);
                }
            }
        });
        JButton chargerButton = new JButton("Charger");
        chargerButton.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setDialogTitle("Sélectionner un fichier .cnc à charger");

            FileNameExtensionFilter cncFilter = new FileNameExtensionFilter(
                    "CNC Files", "cnc"
            );
            fileChooser.setFileFilter(cncFilter);

            fileChooser.setAcceptAllFileFilterUsed(false);

            int userSelection = fileChooser.showOpenDialog(null); // Changed to showOpenDialog for loading

            if (userSelection == JFileChooser.APPROVE_OPTION) {
                File fileToLoad = fileChooser.getSelectedFile();

                System.out.println("Chemin du fichier .cnc à charger : " + fileToLoad.getAbsolutePath());

                try {
                    controleur.load(fileToLoad.getAbsolutePath());
                    System.out.println(controleur.getCNC().getOutilsList().size());
                    refreshAll();
                } catch (IOException ex) {
                    addToConsole(ex.getMessage(), Color.RED);
                }
            }
        });
        JButton AfficherGrille = new JButton("Afficher Grille");


        ActionListener supprimerlistener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                controleur.suppCoupe(selectedElement);
                setSelectedElement(null);
                refreshAll();
            }
        };
        this.enbordureSupprimerButton = new JButton("Supprimer");
        this.enbordureSupprimerButton.addActionListener(supprimerlistener);
        this.enlSupprimerButton = new JButton("Supprimer");
        this.enlSupprimerButton.addActionListener(supprimerlistener);
        this.rectangulaireSupprimerButton = new JButton("Supprimer");
        this.rectangulaireSupprimerButton.addActionListener(supprimerlistener);
        this.paralleleSupprimerButton = new JButton("Supprimer");
        this.paralleleSupprimerButton.addActionListener(supprimerlistener);


        this.erreurPanel = new JPanel(new BorderLayout());
        this.erreurPanel.setPreferredSize(new Dimension(80, 150));


        southPanel.add(erreurPanel, BorderLayout.NORTH);
        JPanel bottomButtonPanel = new JPanel();
        bottomButtonPanel.setLayout(new BorderLayout());
        JPanel ButtonPanel = new JPanel((new FlowLayout(FlowLayout.CENTER, 5, 5)));

        bottomButtonPanel.add(ButtonPanel, BorderLayout.CENTER);

        ButtonPanel.add(sauvegarderButton);
        ButtonPanel.add(chargerButton);
        ButtonPanel.add(exporterButton);
        // ButtonPanel.add(resetZoomButton);
        //ButtonPanel.add(AfficherGrille);
        southPanel.add(bottomButtonPanel, BorderLayout.SOUTH);


        JPanel WHITEPanel = new JPanel();


        dynamicLeftPanel.add(northPanel, BorderLayout.NORTH);
        dynamicLeftPanel.add(WHITEPanel, BorderLayout.CENTER);
        dynamicLeftPanel.add(southPanel, BorderLayout.SOUTH);

        dynamicLeftPanel.setPreferredSize(new Dimension(400, 600));


        // Créer les panneaux dynamiques
        createCoupeParallelePanel();
        createCoupeRectangulairePanel();
        createCoupeEnLPanel();
        createCoupeEnBordurePanel();
        createOutilsPanel();
        createZonesInterditesPanel();
        createPanneauPanel();
        createErreurPanel();


        // Ajouter les actions listeners
        coupeRectangulaireButton.addActionListener(e -> displayRectangulairePanel(null));
        coupeEnLButton.addActionListener(e -> displayEnLPanel(null));
        coupeParallelButton.addActionListener(e -> displayParallelePanel(null));
        coupeEnBordureButton.addActionListener(e -> displayEnBordurePanel(null));
        outilButton.addActionListener(e -> displayOutilPanel());
        Grille.addActionListener(e -> displayGrillePanel());
        zoneInterditeButton.addActionListener(e -> displayZoneInterditePanel());
        panneauButton.addActionListener(e -> displayPanneauPanel());

        // Frame setup
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setTitle("CNC");
        setSize(1920, 1080);
        setLayout(new BorderLayout());

        // Ajouter les panel au panel principal.
        add(dynamicLeftPanel, BorderLayout.WEST);
        add(staticRightPanel, BorderLayout.CENTER);

        setVisible(true);


        // Ajout du listener pour le panel.
        drawingPanel.addMouseListener(new MouseAdapter() {

            @Override
            public void mousePressed(MouseEvent e) {
                Point2D.Double pts = drawingPanel.convertMouseCoord(e);
                System.out.println("Click : CoordP: " + e.getX() + " " + e.getY() + ", CoordN: " + pts.getX() + " " + pts.getY());

                if (isAddingCoupe) {
                    ptsClique.add(pts);

                    if (isAddingCoupeParallele) {
                        ptsClique.clear();
                        AddingCoupeParallele(pts);
                    } else if (isAddingCoupeEnL) {
                        if (ptsClique.size() == 2) {
                            AddingCoupeEnL();
                        }
                    } else if (isAddingCoupeRectangulaire) {
                        if (ptsClique.size() == 3) {
                            AddingCoupeRectangulaire();
                        }
                    }
                } else if (isSelectingParalleleReference) {
                    List<UUID> lignelist = controleur.getLignePanel(pts.getX(), pts.getY());
                    if (!lignelist.isEmpty()) {
                        controleur.modifParallele(selectedElement, lignelist.get(0), null);
                    } else {
                        addToConsole("Aucune ligne de coupe valide trouvée.", Color.RED);
                    }
                    isSelectingParalleleReference = false;

                } else if (isSelectingRectangulaireReference) {
                    try {
                        controleur.modifRectangulaire(selectedElement, pts.getX(), pts.getY(), null, null, null, null);
                        rectangulaireReferenceXField.setText(String.format(Locale.US, "%.3f", pts.getX()));
                        rectangulaireReferenceYField.setText(String.format(Locale.US, "%.3f", pts.getY()));
                    } catch (Exception ee) {
                        addToConsole(ee.toString(), Color.RED);
                    }
                    isSelectingRectangulaireReference = false;
                } else if (isSelectingRectangulaireCoin1) {
                    controleur.modifRectangulaire(selectedElement, null, null, pts.getX(), pts.getY(), null, null);
                    rectangulaireCoin1XField.setText(String.format(Locale.US, "%.3f", pts.getX()));
                    rectangulaireCoin1YField.setText(String.format(Locale.US, "%.3f", pts.getY()));
                    isSelectingRectangulaireCoin1 = false;
                } else if (isSelectingRectangulaireCoin2) {
                    controleur.modifRectangulaire(selectedElement, null, null, null, null, pts.getX(), pts.getY());
                    rectangulaireCoin2XField.setText(String.format(Locale.US, "%.3f", pts.getX()));
                    rectangulaireCoin2YField.setText(String.format(Locale.US, "%.3f", pts.getY()));
                    isSelectingRectangulaireCoin2 = false;
                } else if (isSelectingEnLCoin1) {
                    controleur.modifEnL(selectedElement, null, null, pts.getX(), pts.getY());
                    enlCoin1XField.setText(String.format(Locale.US, "%.3f", pts.getX()));
                    enlCoin1YField.setText(String.format(Locale.US, "%.3f", pts.getY()));
                    isSelectingEnLCoin1 = false;
                } else if (isSelectingEnLCoin2) {
                    try {
                        controleur.modifEnL(selectedElement, pts.getX(), pts.getY(), null, null);
                    } catch (Exception ee) {
                        addToConsole(ee.getMessage(), Color.RED);
                    }
                    enlCoin2XField.setText(String.format(Locale.US, "%.3f", pts.getX()));
                    enlCoin2YField.setText(String.format(Locale.US, "%.3f", pts.getY()));
                    isSelectingEnLCoin2 = false;
                } else {
                    UUID elemId = controleur.getElemPanel(pts.getX(), pts.getY());
                    if (elemId != null) {
                        draggedCoupeId = elemId;
                        initialClickPoint = pts;
                        setSelectedElement(elemId);
                        updateDynamicPanelByUUID(elemId);
                        System.out.println("Clicked on " + elemId);
                    } else {
                        clearDynamicPanel();
                        setSelectedElement(null);
                    }
                }
                refreshAll();
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                if (draggedCoupeId != null) {
                    // Finalize drag move
                    controleur.finalizeMove(draggedCoupeId);
                    draggedCoupeId = null;
                    initialClickPoint = null;
                    refreshAll();
                }
            }
        });

        drawingPanel.addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                if (draggedCoupeId != null && initialClickPoint != null) {
                    controleur.setMoving(true);
                    Point2D.Double currentMousePoint = drawingPanel.convertMouseCoord(e);

                    // Calculate the offset of the mouse movement
                    double deltaX = currentMousePoint.getX() - initialClickPoint.getX();
                    double deltaY = currentMousePoint.getY() - initialClickPoint.getY();

                    // Apply the movement to the coupe
                    applyDragToCoupe(draggedCoupeId, deltaX, deltaY);
                    initialClickPoint = currentMousePoint; // Update initialClickPoint for smooth dragging
                    refreshAll();
                    controleur.setMoving(false);
                }
            }
        });
        displayPanneauPanel();
    }

    private void applyDragToCoupe(UUID coupeId, double deltaX, double deltaY) {
        // Check which type of coupe it is and update its position
        RectangulaireDTO rectangulaire = findRectangulaireById(coupeId);
        if (rectangulaire != null) {
            controleur.modifRectangulaire(
                    coupeId,
                    rectangulaire.getPoint(1).getX() + deltaX,
                    rectangulaire.getPoint(1).getY() + deltaY,
                    rectangulaire.getPoint(2).getX() + deltaX,
                    rectangulaire.getPoint(2).getY() + deltaY,
                    rectangulaire.getPoint(3).getX() + deltaX,
                    rectangulaire.getPoint(3).getY() + deltaY
            );
            return;
        }

        ParalleleDTO parallele = findParalleleById(coupeId);
        if (parallele != null) {
            controleur.modifParallele(coupeId, null, parallele.getTaille());
            return;
        }

        EnLDTO enl = findEnLById(coupeId);
        if (enl != null) {
            controleur.modifEnL(
                    coupeId,
                    enl.getCoin().getX() + deltaX,
                    enl.getCoin().getY() + deltaY,
                    enl.getCoinOppose().getX() + deltaX,
                    enl.getCoinOppose().getY() + deltaY
            );
            return;
        }

        EnBordureDTO enBordure = findEnBordureById(coupeId);
        if (enBordure != null) {
            controleur.modifEnBordure(
                    coupeId,
                    enBordure.getDimensionsFinales().getLargeur(),
                    enBordure.getDimensionsFinales().getHauteur()
            );
        }
    }

    private RectangulaireDTO findRectangulaireById(UUID coupeId) {
        return currentdto.getRectangulaireListe().stream()
                .filter(coupe -> coupe.getId().equals(coupeId))
                .findFirst()
                .orElse(null);
    }

    private ParalleleDTO findParalleleById(UUID coupeId) {
        return currentdto.getParalleleListe().stream()
                .filter(coupe -> coupe.getId().equals(coupeId))
                .findFirst()
                .orElse(null);
    }

    private EnLDTO findEnLById(UUID coupeId) {
        return currentdto.getEnLListe().stream()
                .filter(coupe -> coupe.getId().equals(coupeId))
                .findFirst()
                .orElse(null);
    }

    private EnBordureDTO findEnBordureById(UUID coupeId) {
        return currentdto.getEnBordureListe().stream()
                .filter(coupe -> coupe.getId().equals(coupeId))
                .findFirst()
                .orElse(null);
    }


    private void updateDynamicPanelByUUID(UUID elemId) {
        isSwitchingPanel = true;
        setSelectedElement(elemId);
        if (elemId == currentdto.getPanneau().getId()) {
            clearDynamicPanel();
            refreshAll();
            isSwitchingPanel = false;
            return;
        }
        for (RectangulaireDTO rec : currentdto.getRectangulaireListe()) {
            if (rec.getId() == elemId) {
                displayRectangulairePanel(rec);
                refreshAll();
                isSwitchingPanel = false;
                return;
            }
        }
        for (ParalleleDTO para : currentdto.getParalleleListe()) {
            if (para.getId() == elemId) {
                displayParallelePanel(para);
                refreshAll();
                isSwitchingPanel = false;
                return;
            }
        }
        for (EnBordureDTO bordu : currentdto.getEnBordureListe()) {
            if (bordu.getId() == elemId) {
                displayEnBordurePanel(bordu);
                refreshAll();
                isSwitchingPanel = false;
                return;
            }
        }
        for (EnLDTO enl : currentdto.getEnLListe()) {
            if (enl.getId() == elemId) {
                displayEnLPanel(enl);
                refreshAll();
                isSwitchingPanel = false;
                return;
            }
        }
    }

    private void updateDynamicPanel(JPanel pane) {
        JPanel WHITEPanel = (JPanel) dynamicLeftPanel.getComponent(1);
        WHITEPanel.removeAll();
        WHITEPanel.add(pane, BorderLayout.CENTER);
        WHITEPanel.revalidate();
        WHITEPanel.repaint();
    }


    private void displayRectangulairePanel(RectangulaireDTO rec) {
        disableAddingCoupe();
        if (rec == null) {
            AddingRectangulaire();
        } else {
            editRectangulaire(rec);
        }
        updateDynamicPanel(panelRectangulaire);
    }


    private void updateOutilsList(OutilDTO selectedOutil) {
        isSwitchingPanel = true;
        List<OutilDTO> listeOutils = currentdto.getOutilsList();
        List<String> nomsOutils = new ArrayList<>();
        for (OutilDTO outil : listeOutils) {
            nomsOutils.add(outil.getNom());
        }
        outilListe.setListData(nomsOutils.toArray(new String[0]));

        if (selectedOutil != null) {
            int selectedIndex = -1;
            for (int i = 0; i < listeOutils.size(); i++) {
                if (listeOutils.get(i).getId() == selectedOutil.getId()) {
                    selectedIndex = i;
                    break;
                }
            }
            System.out.println("Selectionne : " + selectedIndex);
            if (selectedIndex != -1) {
                outilListe.setSelectedIndex(selectedIndex);
                outilNom.setText(currentdto.getCurrOutil().getNom());
                outilDiametre.setText(String.format(Locale.US, "%.3f", currentdto.getCurrOutil().getDiametre()));
                System.out.println("Current outil sélectionné : " + selectedOutil.getNom());
            }
        } else {
            outilListe.clearSelection();
        }
        outilListe.revalidate();
        outilListe.repaint();
        isSwitchingPanel = false;
    }

    private JPanel panelgrille(boolean t) {
        JPanel inputKISTECOUPE = new JPanel();
        inputKISTECOUPE.setLayout(new GridLayout(4, 1, 2, 10));
        JComboBox comboBox = new JComboBox();
        comboBox.addItem("mm");
        comboBox.addItem("pouce");

        JButton MagnetiserGrille = new JButton("Magnetiser la Grille");
        MagnetiserGrille.addActionListener(e -> {
            MagnetiserlaGrille = true;
            drawingPanel.setMagnetiserlaGrille1();
        });

        JButton AfficherGrille = new JButton("Enlever Grille");

        ActionListener AfficheGrillelistener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                drawingPanel.hideGrid();
                refreshAll();
            }
        };

        AfficherGrille.addActionListener(AfficheGrillelistener);


        JTextField tailleDelaGrille = new JTextField();


        JButton saveButton = new JButton("Afficher");
        saveButton.addActionListener(e -> {
            String gridSizeText = tailleDelaGrille.getText();

            String selectedUnit = (String) comboBox.getSelectedItem();

            try {
                double gridSize = Double.parseDouble(gridSizeText);
                System.out.println("Grid Size: " + gridSize + " " + selectedUnit);

                drawingPanel.getsize(gridSizeText);
                drawingPanel.getmeasurement(selectedUnit);
                System.out.println("here");
                drawingPanel.setshowgrille();
                refreshAll();

            } catch (NumberFormatException ex) {
                addToConsole("Veuillez une taille de grille valide.", Color.red);
            }
        });

        JPanel PanelOftailleandcombo = new JPanel();
        PanelOftailleandcombo.setLayout(new GridLayout(1, 2, 2, 2));

        PanelOftailleandcombo.add(tailleDelaGrille);
        PanelOftailleandcombo.add(comboBox);

        JPanel spacerTop = new JPanel();

        inputKISTECOUPE.add(MagnetiserGrille);
        inputKISTECOUPE.add(PanelOftailleandcombo);
        inputKISTECOUPE.add(saveButton);
        inputKISTECOUPE.add(AfficherGrille);


        return inputKISTECOUPE;

    }

    private void displayParallelePanel(ParalleleDTO para) {
        disableAddingCoupe();
        if (para == null) {
            addingParallele();
        } else {
            isEditingParallele = true;
            editParallele(para);
        }
        updateDynamicPanel(panelParallele);
    }

    private void addingParallele() {
        setSelectedElement(null);
        isEditingParallele = false;
        paralleleTailleField.setText("");
        paralleleTailleField.setEditable(true);
        paralleleReferenceButton.setEnabled(false);
        paralleleSupprimerButton.setEnabled(false);
        paralleleProfondeurField.setEditable(false);
        paralleleProfondeurField.setText(String.format(Locale.US, "%.3f", currentdto.getProfondeur()));
        updateOutilComboBox(paralleleOutilComboBox, currentdto.getCurrOutil());
        paralleleOutilComboBox.setEnabled(false);
    }

    private void editParallele(ParalleleDTO parallele) {
        isEditingParallele = true;
        paralleleReferenceButton.setEnabled(true);
        paralleleSupprimerButton.setEnabled(true);
        paralleleTailleField.setText(String.format(Locale.US, "%.3f", parallele.getTaille()));
        paralleleProfondeurField.setText(String.format(Locale.US, "%.3f", parallele.getProfondeur()));

        paralleleTailleField.setEditable(true);
        paralleleProfondeurField.setEditable(true);
        updateOutilComboBox(paralleleOutilComboBox, parallele.getOutil());
        paralleleOutilComboBox.setEnabled(true);

    }

    private void AddingRectangulaire() {
        setSelectedElement(null);
        rectangulaireCoin1XField.setText("");
        rectangulaireCoin1YField.setText("");
        rectangulaireCoin2XField.setText("");
        rectangulaireCoin2YField.setText("");
        rectangulaireReferenceXField.setText("");
        rectangulaireReferenceYField.setText("");
        rectangulaireProfondeurField.setText(String.format(Locale.US, "%.3f", currentdto.getProfondeur()));

        rectangulaireCoin1XField.setEditable(false);
        rectangulaireCoin1YField.setEditable(false);
        rectangulaireCoin2XField.setEditable(false);
        rectangulaireCoin2YField.setEditable(false);
        rectangulaireReferenceXField.setEditable(false);
        rectangulaireReferenceYField.setEditable(false);
        rectangulaireProfondeurField.setEditable(false);
        rectangulaireReferenceButton.setEnabled(false);
        rectangulaireCoin1Button.setEnabled(false);
        rectangulaireCoin2Button.setEnabled(false);
        rectangulaireSupprimerButton.setEnabled(false);
        updateOutilComboBox(rectangulaireOutilComboBox, currentdto.getCurrOutil());
        rectangulaireOutilComboBox.setEnabled(false);

        isAddingCoupeRectangulaire = true;
        isAddingCoupe = true;
    }

    private void editRectangulaire(RectangulaireDTO rectangulaire) {
        rectangulaireCoin1XField.setText(String.format(Locale.US, "%.3f", rectangulaire.getPoint(2).getX()));
        rectangulaireCoin1YField.setText(String.format(Locale.US, "%.3f", rectangulaire.getPoint(2).getY()));
        rectangulaireCoin2XField.setText(String.format(Locale.US, "%.3f", rectangulaire.getPoint(3).getX()));
        rectangulaireCoin2YField.setText(String.format(Locale.US, "%.3f", rectangulaire.getPoint(3).getY()));
        rectangulaireReferenceXField.setText(String.format(Locale.US, "%.3f", rectangulaire.getPoint(1).getX()));
        rectangulaireReferenceYField.setText(String.format(Locale.US, "%.3f", rectangulaire.getPoint(1).getY()));
        rectangulaireProfondeurField.setText(String.format(Locale.US, "%.3f", rectangulaire.getProfondeur()));

        rectangulaireCoin1XField.setEditable(true);
        rectangulaireCoin1YField.setEditable(true);
        rectangulaireCoin2XField.setEditable(true);
        rectangulaireCoin2YField.setEditable(true);
        rectangulaireReferenceXField.setEditable(true);
        rectangulaireReferenceYField.setEditable(true);
        rectangulaireProfondeurField.setEditable(true);

        rectangulaireReferenceButton.setEnabled(true);
        rectangulaireCoin1Button.setEnabled(true);
        rectangulaireCoin2Button.setEnabled(true);
        rectangulaireSupprimerButton.setEnabled(true);

        updateOutilComboBox(rectangulaireOutilComboBox, rectangulaire.getOutil());
        rectangulaireOutilComboBox.setEnabled(true);

    }

    private void displayEnLPanel(EnLDTO enl) {
        disableAddingCoupe();
        if (enl == null) {
            AddingEnL();
        } else {
            EditEnL(enl);
        }
        updateDynamicPanel(panelEnL);
    }

    private void AddingEnL() {
        setSelectedElement(null);
        enlCoin1XField.setText("");
        enlCoin1YField.setText("");
        enlCoin2XField.setText("");
        enlCoin2YField.setText("");
        enlProfondeurField.setText(String.format(Locale.US, "%.3f", currentdto.getProfondeur()));
        enlCoin1XField.setEditable(false);
        enlCoin1YField.setEditable(false);
        enlCoin2XField.setEditable(false);
        enlCoin2YField.setEditable(false);
        enlProfondeurField.setEditable(false);
        enlCoin1Button.setEnabled(false);
        enlCoin2Button.setEnabled(false);
        enlSupprimerButton.setEnabled(false);
        updateOutilComboBox(enlOutilComboBox, currentdto.getCurrOutil());
        enlOutilComboBox.setEnabled(false);
        isAddingCoupeEnL = true;
        isAddingCoupe = true;
    }

    private void EditEnL(EnLDTO enl) {
        enlCoin1XField.setEditable(true);
        enlCoin1YField.setEditable(true);
        enlCoin2XField.setEditable(true);
        enlCoin2YField.setEditable(true);
        enlProfondeurField.setEditable(true);

        enlCoin1Button.setEnabled(true);
        enlCoin2Button.setEnabled(true);
        enlSupprimerButton.setEnabled(true);
        updateOutilComboBox(enlOutilComboBox, enl.getOutil());
        enlOutilComboBox.setEnabled(true);

        enlProfondeurField.setText(String.format(Locale.US, "%.3f", enl.getProfondeur()));
        enlCoin1XField.setText(String.format(Locale.US, "%.3f", enl.getCoinOppose().getX()));
        enlCoin1YField.setText(String.format(Locale.US, "%.3f", enl.getCoinOppose().getY()));
        enlCoin2XField.setText(String.format(Locale.US, "%.3f", enl.getCoin().getX()));
        enlCoin2YField.setText(String.format(Locale.US, "%.3f", enl.getCoin().getY()));

    }


    private void displayEnBordurePanel(EnBordureDTO bordu) {
        disableAddingCoupe();
        if (bordu == null) {
            addingEnBordure();
        } else {
            editEnBordure(bordu);
        }
        updateDynamicPanel(panelEnBordure);
    }

    private void addingEnBordure() {
        setSelectedElement(null);
        enbordureLargeurField.setText("");
        enbordureHauteurField.setText("");
        enbordureProfondeurField.setText(String.format(Locale.US, "%.3f", currentdto.getProfondeur()));
        enbordureProfondeurField.setEditable(false);
        enbordureLargeurField.setEditable(true);
        enbordureHauteurField.setEditable(true);
        enbordureAppliquerButton.setEnabled(true);
        enbordureSupprimerButton.setEnabled(false);
        updateOutilComboBox(enbordureOutilComboBox, currentdto.getCurrOutil());
        enbordureOutilComboBox.setEnabled(false);

    }

    private void editEnBordure(EnBordureDTO bordu) {
        isEditingEnBordure = true;
        enbordureLargeurField.setEditable(true);
        enbordureHauteurField.setEditable(true);
        enbordureProfondeurField.setEditable(true);
        enbordureAppliquerButton.setEnabled(true);
        enbordureSupprimerButton.setEnabled(true);

        enbordureProfondeurField.setText(String.format(Locale.US, "%.3f", bordu.getProfondeur()));
        enbordureLargeurField.setText(String.format(Locale.US, "%.3f", bordu.getDimensionsFinales().getLargeur()));
        enbordureHauteurField.setText(String.format(Locale.US, "%.3f", bordu.getDimensionsFinales().getHauteur()));
        updateOutilComboBox(enbordureOutilComboBox, bordu.getOutil());
        enbordureOutilComboBox.setEnabled(true);
    }

    private void displayOutilPanel() {
        isSwitchingPanel = true;
        disableAddingCoupe();
        setSelectedElement(null);
        refreshAll();
        updateOutilsList(currentdto.getCurrOutil());
        refreshAll();
        updateDynamicPanel(panelOutils);
        isSwitchingPanel = false;
    }

    private void displayGrillePanel() {
        isSwitchingPanel = true;
        disableAddingCoupe();
        updateDynamicPanel(panelgrille(true));
        isSwitchingPanel = false;
    }


    private void displayZoneInterditePanel() {
        isSwitchingPanel = true;
        disableAddingCoupe();
        setSelectedElement(null);
        updateDynamicPanel(panelZonesInterdites);
        isSwitchingPanel = false;
    }

    private void displayPanneauPanel() {
        disableAddingCoupe();
        updateDynamicPanel(panelPanneau);

    }

    private void createErreurPanel() {
        consoleTextArea = new JTextArea(5, 30);
        consoleTextArea.setEditable(false);
        consoleTextArea.setForeground(Color.BLACK);
        consoleTextArea.setBackground(Color.WHITE);
        consoleTextArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        consoleTextArea.append("Bienvenue sur la CNC !\n");

        JScrollPane console = new JScrollPane(consoleTextArea);
        console.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        console.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        this.erreurPanel.add(console);
    }

    private void addToConsole(String message, Color color) {
        consoleTextArea.setForeground(Color.BLACK);
        consoleTextArea.append(message + "\n");
        consoleTextArea.setCaretPosition(consoleTextArea.getDocument().getLength());
    }

    {
// GUI initializer generated by IntelliJ IDEA GUI Designer
// >>> IMPORTANT!! <<<
// DO NOT EDIT OR ADD ANY CODE HERE!
        $$$setupUI$$$();
    }

    /**
     * Method generated by IntelliJ IDEA GUI Designer
     * >>> IMPORTANT!! <<<
     * DO NOT edit this method OR call it in your code!
     *
     * @noinspection ALL
     */
    private void $$$setupUI$$$() {
        final JPanel panel1 = new JPanel();
        panel1.setLayout(new GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
    }


    // FUNCTION BOUTONS

    private void refreshAll() {
        this.currentdto = controleur.getCNC();
        this.drawingPanel.setCurrentDTO(currentdto);
        this.drawingPanel.repaint();
        this.exporterButton.setEnabled(controleur.isCNCValid());
        repaint();
    }

    private void setSelectedElement(UUID id) {
        this.selectedElement = id;
        drawingPanel.setSelectedElement(id);
    }

    private void disableAddingCoupe() {
        this.isAddingCoupeParallele = false;
        this.isAddingCoupeEnL = false;
        this.isAddingCoupeRectangulaire = false;
        this.isAddingCoupe = false;
        this.isSelectingParalleleReference = false;
        this.isSelectingRectangulaireReference = false;
        this.isSelectingRectangulaireCoin1 = false;
        this.isSelectingRectangulaireCoin2 = false;
        this.isSelectingEnLCoin1 = false;
        this.isSelectingEnLCoin2 = false;
        this.isEditingEnBordure = false;
        this.ptsClique.clear();
    }

    private void AddingCoupeEnL() {
        try {
            List<UUID> ref_ligne = controleur.getLignePanel(ptsClique.get(0).getX(), ptsClique.get(0).getY());
            if (1 < ref_ligne.size()) {
                UUID newEnLID = controleur.addEnL(ref_ligne, ptsClique.get(1).getX(), ptsClique.get(1).getY());
                enlCoin1XField.setText(String.format(Locale.US, "%.3f", ptsClique.get(0).getX()));
                enlCoin1YField.setText(String.format(Locale.US, "%.3f", ptsClique.get(0).getY()));
                enlCoin2XField.setText(String.format(Locale.US, "%.3f", ptsClique.get(1).getX()));
                enlCoin2YField.setText(String.format(Locale.US, "%.3f", ptsClique.get(1).getY()));
                addToConsole("Coupe en L ajoutée !", Color.GREEN);
                disableAddingCoupe();
                refreshAll();
                updateDynamicPanelByUUID(newEnLID);
            } else {
                addToConsole("Le deuxième point ne correspond pas à une intersection.", Color.RED);
                ptsClique.clear();
            }
        } catch (Exception e) {
            addToConsole(e.getMessage(), Color.RED);
            disableAddingCoupe();
        }
    }

    private void AddingCoupeRectangulaire() {

        try {
            UUID newRecId = controleur.addRectangulaire(ptsClique.get(0).getX(), ptsClique.get(0).getY(), ptsClique.get(1).getX(), ptsClique.get(1).getY(), ptsClique.get(2).getX(), ptsClique.get(2).getY());
            rectangulaireReferenceXField.setText(String.format(Locale.US, "%.3f", ptsClique.get(0).getX()));
            rectangulaireReferenceYField.setText(String.format(Locale.US, "%.3f", ptsClique.get(0).getY()));
            rectangulaireCoin1XField.setText(String.format(Locale.US, "%.3f", ptsClique.get(1).getX()));
            rectangulaireCoin1YField.setText(String.format(Locale.US, "%.3f", ptsClique.get(1).getY()));
            rectangulaireCoin2XField.setText(String.format(Locale.US, "%.3f", ptsClique.get(2).getX()));
            rectangulaireCoin2YField.setText(String.format(Locale.US, "%.3f", ptsClique.get(2).getY()));
            addToConsole("Coupe rectangulaire ajoutée !", Color.GREEN);
            disableAddingCoupe();
            refreshAll();
            updateDynamicPanelByUUID(newRecId);
        } catch (Exception e) {
            addToConsole(e.getMessage(), Color.RED);
            disableAddingCoupe();
        }
    }

    private void AddingCoupeParallele(Point2D.Double pts) {
        try {
            List<UUID> ref_ligne = controleur.getLignePanel(pts.getX(), pts.getY());
            if (!ref_ligne.isEmpty()) {
                UUID newparaid = controleur.addParallele(ref_ligne.get(0), Double.valueOf(paralleleTailleField.getText()).doubleValue());
                addToConsole("Coupe parallèle ajoutée !", Color.GREEN);
                disableAddingCoupe();
                refreshAll();
                updateDynamicPanelByUUID(newparaid);
            }
        } catch (Exception e) {
            addToConsole(e.getMessage(), Color.RED);
            disableAddingCoupe();
        }
    }

    private void createCoupeParallelePanel() {
        JLabel titleLabel = new JLabel("Coupe parallèle", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 16));

        JPanel panelpara1 = new JPanel();
        panelpara1.setLayout(new GridLayout(4, 3, 5, 5));

        this.paralleleTailleField = new JTextField(10);
        this.paralleleProfondeurField = new JTextField(10);

        paralleleReferenceButton = new JButton("Reference");

        panelpara1.add(new JLabel("Taille :"));
        panelpara1.add(paralleleTailleField);
        panelpara1.add(new JLabel("mm"));

        panelpara1.add(new JLabel("Profondeur :"));
        panelpara1.add(paralleleProfondeurField);
        panelpara1.add(new JLabel("mm"));

        panelpara1.add(new JLabel("Ligne de référence :"));
        panelpara1.add(paralleleReferenceButton);
        panelpara1.add(new JLabel());

        paralleleReferenceButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ee) {
                disableAddingCoupe();
                isSelectingParalleleReference = true;
            }
        });

        FocusAdapter parallelelistener = new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                if (!isSwitchingPanel) {
                    Double taille = getValidDigitValue(paralleleTailleField);
                    if (isEditingParallele && !isAddingCoupeParallele) {
                        Double profondeur = getValidDigitValue(paralleleProfondeurField);
                        controleur.modifCoupeProfondeur(selectedElement, profondeur);
                        controleur.modifParallele(selectedElement, null, taille);
                        refreshAll();
                    } else {
                        if (taille != null) {
                            paralleleTailleField.setEditable(false);
                            isAddingCoupeParallele = true;
                            isAddingCoupe = true;
                        } else {
                            isAddingCoupeParallele = false;
                            isAddingCoupe = false;
                        }
                    }
                }
            }
        };

        paralleleTailleField.addFocusListener(parallelelistener);
        paralleleProfondeurField.addFocusListener(parallelelistener);

        JPanel panelBouton = new JPanel();
        panelBouton.setLayout(new FlowLayout(FlowLayout.CENTER));
        panelBouton.add(paralleleSupprimerButton);

        this.paralleleOutilComboBox = new JComboBox<>();

        paralleleOutilComboBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!isSwitchingPanel) {
                    if (paralleleOutilComboBox.getSelectedIndex() != -1 && selectedElement != null) {
                        controleur.modifCoupeOutil(selectedElement, currentdto.getOutilsList().get(paralleleOutilComboBox.getSelectedIndex()).getId());
                        refreshAll();
                    }
                }
            }
        });

        JPanel outilMiniPanel = new JPanel();
        outilMiniPanel.setLayout(new BoxLayout(outilMiniPanel, BoxLayout.X_AXIS));
        outilMiniPanel.add(new JLabel("Outil : "));
        outilMiniPanel.add(paralleleOutilComboBox);

        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.add(outilMiniPanel, BorderLayout.NORTH);
        bottomPanel.add(panelBouton, BorderLayout.SOUTH);

        this.panelParallele = new JPanel();
        this.panelParallele.setLayout(new BorderLayout(5, 5));
        this.panelParallele.add(titleLabel, BorderLayout.NORTH);
        this.panelParallele.add(panelpara1, BorderLayout.CENTER);
        this.panelParallele.add(bottomPanel, BorderLayout.SOUTH);
    }


    private void createCoupeRectangulairePanel() {
        JLabel titleLabel = new JLabel("Coupe rectangulaire", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 16));

        this.rectangulaireReferenceXField = new JTextField(10);
        this.rectangulaireReferenceYField = new JTextField(10);
        this.rectangulaireCoin1XField = new JTextField(10);
        this.rectangulaireCoin1YField = new JTextField(10);
        this.rectangulaireCoin2XField = new JTextField(10);
        this.rectangulaireCoin2YField = new JTextField(10);

        this.rectangulaireReferenceButton = new JButton("Reference");
        this.rectangulaireCoin1Button = new JButton("Coin 1");
        this.rectangulaireCoin2Button = new JButton("Coin 2");

        rectangulaireReferenceButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ee) {
                disableAddingCoupe();
                isSelectingRectangulaireReference = true;
            }
        });

        rectangulaireCoin1Button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ee) {
                disableAddingCoupe();
                isSelectingRectangulaireCoin1 = true;
            }
        });

        rectangulaireCoin2Button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ee) {
                disableAddingCoupe();
                isSelectingRectangulaireCoin2 = true;
            }
        });

        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new GridLayout(5, 3, 2, 2));

        inputPanel.add(rectangulaireReferenceXField);
        inputPanel.add(rectangulaireReferenceYField);
        inputPanel.add(rectangulaireReferenceButton);

        inputPanel.add(rectangulaireCoin1XField);
        inputPanel.add(rectangulaireCoin1YField);
        inputPanel.add(rectangulaireCoin1Button);

        inputPanel.add(rectangulaireCoin2XField);
        inputPanel.add(rectangulaireCoin2YField);
        inputPanel.add(rectangulaireCoin2Button);

        this.rectangulaireProfondeurField = new JTextField(10);

        inputPanel.add(new JLabel("Profondeur :"));
        inputPanel.add(rectangulaireProfondeurField);
        inputPanel.add(new JLabel("mm"));

        FocusListener listenerRec = new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent ee) {
                Double coin1x = getValidDigitValue(rectangulaireCoin1XField);
                Double coin1y = getValidDigitValue(rectangulaireCoin1YField);
                Double coin2x = getValidDigitValue(rectangulaireCoin2XField);
                Double coin2y = getValidDigitValue(rectangulaireCoin2YField);
                Double referencex = getValidDigitValue(rectangulaireReferenceXField);
                Double referencey = getValidDigitValue(rectangulaireReferenceYField);
                Double profondeur = getValidDigitValue(rectangulaireProfondeurField);
                if (!isSwitchingPanel) {
                    controleur.modifCoupeProfondeur(selectedElement, profondeur);
                    controleur.modifRectangulaire(selectedElement, referencex, referencey, coin1x, coin1y, coin2x, coin2y);
                    refreshAll();
                }
            }
        };

        rectangulaireCoin1XField.addFocusListener(listenerRec);
        rectangulaireCoin1YField.addFocusListener(listenerRec);
        rectangulaireCoin2XField.addFocusListener(listenerRec);
        rectangulaireCoin2YField.addFocusListener(listenerRec);
        rectangulaireReferenceXField.addFocusListener(listenerRec);
        rectangulaireReferenceYField.addFocusListener(listenerRec);
        rectangulaireProfondeurField.addFocusListener(listenerRec);

        JPanel panelBouton = new JPanel();
        panelBouton.setLayout(new FlowLayout(FlowLayout.CENTER));
        panelBouton.add(rectangulaireSupprimerButton);

        this.panelRectangulaire = new JPanel();
        this.panelRectangulaire.setLayout(new BorderLayout(5, 5));

        this.panelRectangulaire.add(titleLabel, BorderLayout.NORTH);
        this.panelRectangulaire.add(inputPanel, BorderLayout.CENTER);

        this.rectangulaireOutilComboBox = new JComboBox<>();

        rectangulaireOutilComboBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!isSwitchingPanel) {
                    if (rectangulaireOutilComboBox.getSelectedIndex() != -1 && selectedElement != null) {
                        controleur.modifCoupeOutil(selectedElement, currentdto.getOutilsList().get(rectangulaireOutilComboBox.getSelectedIndex()).getId());
                        refreshAll();
                    }
                }
            }
        });


        JPanel outilMiniPanel = new JPanel();
        outilMiniPanel.setLayout(new BoxLayout(outilMiniPanel, BoxLayout.X_AXIS));
        outilMiniPanel.add(new JLabel("Outil : "));
        outilMiniPanel.add(rectangulaireOutilComboBox);

        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.add(outilMiniPanel, BorderLayout.NORTH);
        bottomPanel.add(panelBouton, BorderLayout.SOUTH);

        this.panelRectangulaire.add(bottomPanel, BorderLayout.SOUTH);
    }


    private void createCoupeEnBordurePanel() {
        JLabel titleLabel = new JLabel("Coupe en bordure", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 16));

        JPanel enbordure = new JPanel();
        enbordure.setLayout(new GridLayout(3, 3, 5, 5)); // 4 lignes pour inclure le bouton Appliquer

        JPanel dimlabel = new JPanel();
        dimlabel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 10));
        dimlabel.add(new JLabel("Largeur : "));

        this.enbordureLargeurField = new JTextField(10);
        JPanel testfi = new JPanel();
        testfi.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 10));
        testfi.add(enbordureLargeurField);

        JPanel dimlabel2 = new JPanel();
        dimlabel2.setLayout(new FlowLayout(FlowLayout.CENTER, 0, 10));
        dimlabel2.add(new JLabel("Hauteur : "));

        this.enbordureHauteurField = new JTextField(10);
        JPanel testfi2 = new JPanel();
        testfi2.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 10));
        testfi2.add(enbordureHauteurField);

        // Bouton Appliquer
        this.enbordureAppliquerButton = new JButton("Appliquer");
        enbordureAppliquerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ee) {
                Double largeur = getValidDigitValue(enbordureLargeurField);
                Double hauteur = getValidDigitValue(enbordureHauteurField);
                if (isEditingEnBordure && !isSwitchingPanel) {
                    Double profondeur = getValidDigitValue(enbordureProfondeurField);
                    controleur.modifCoupeProfondeur(selectedElement, profondeur);
                    controleur.modifEnBordure(selectedElement, largeur, hauteur);
                    refreshAll();
                } else {
                    if (largeur != null && hauteur != null) {
                        try {
                            UUID newenbid = controleur.addEnBordure(largeur, hauteur);
                            addToConsole("Coupe en bordure ajoutée !", Color.GREEN);
                            enbordureAppliquerButton.setEnabled(false);
                            refreshAll();
                            updateDynamicPanelByUUID(newenbid);
                        } catch (Exception e) {
                            addToConsole(e.getMessage(), Color.RED);
                        }
                    } else {
                        addToConsole("Dimensions invalides.", Color.RED);
                    }
                }
            }
        });


        JPanel dimlabel3 = new JPanel();
        dimlabel3.setLayout(new FlowLayout(FlowLayout.CENTER, 0, 10));
        dimlabel3.add(new JLabel("Profondeur : "));

        this.enbordureProfondeurField = new JTextField(10);
        JPanel testfi3 = new JPanel();
        testfi3.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 10));
        testfi3.add(enbordureProfondeurField);

        enbordure.add(dimlabel);
        enbordure.add(testfi);
        enbordure.add(new JLabel("mm"));
        enbordure.add(dimlabel2);
        enbordure.add(testfi2);
        enbordure.add(new JLabel("mm"));
        enbordure.add(dimlabel3);
        enbordure.add(testfi3);
        enbordure.add(new JLabel("mm"));

        // Liste d'outils
        this.enbordureOutilComboBox = new JComboBox<>();
        enbordureOutilComboBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!isSwitchingPanel) {
                    if (enbordureOutilComboBox.getSelectedIndex() != -1 && selectedElement != null) {
                        controleur.modifCoupeOutil(selectedElement,
                                currentdto.getOutilsList().get(enbordureOutilComboBox.getSelectedIndex()).getId());
                        refreshAll();
                    }
                }
            }
        });

        JPanel outilMiniPanel = new JPanel();
        outilMiniPanel.setLayout(new BoxLayout(outilMiniPanel, BoxLayout.X_AXIS));
        outilMiniPanel.add(new JLabel("Outil : "));
        outilMiniPanel.add(enbordureOutilComboBox);

        // Bouton Supprimer

        JPanel panelBouton = new JPanel(new GridLayout(1, 2, 1, 1));
        panelBouton.add(enbordureSupprimerButton);
        panelBouton.add(enbordureAppliquerButton);

        // Panneau Bas : Liste et Supprimer
        JPanel bottomPanel = new JPanel(new BorderLayout(5, 5));
        bottomPanel.add(outilMiniPanel, BorderLayout.CENTER);
        bottomPanel.add(panelBouton, BorderLayout.SOUTH);

        this.panelEnBordure = new JPanel();
        this.panelEnBordure.setLayout(new BorderLayout(5, 5));
        this.panelEnBordure.add(titleLabel, BorderLayout.NORTH);
        this.panelEnBordure.add(enbordure, BorderLayout.CENTER);
        this.panelEnBordure.add(bottomPanel, BorderLayout.SOUTH);
    }


    private void createCoupeEnLPanel() {
        JLabel titleLabel = new JLabel("Coupe En L", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 16));

        this.enlCoin1XField = new JTextField(10);
        this.enlCoin1YField = new JTextField(10);
        this.enlCoin2XField = new JTextField(10);
        this.enlCoin2YField = new JTextField(10);

        this.enlCoin1Button = new JButton("Coin 1");
        this.enlCoin2Button = new JButton("Coin 2");

        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new GridLayout(3, 3, 2, 2));

        inputPanel.add(enlCoin1XField);
        inputPanel.add(enlCoin1YField);
        inputPanel.add(enlCoin1Button);

        inputPanel.add(enlCoin2XField);
        inputPanel.add(enlCoin2YField);
        inputPanel.add(enlCoin2Button);

        JLabel prof = new JLabel("Profondeur :");
        this.enlProfondeurField = new JTextField(10);
        JLabel cm = new JLabel("mm");

        inputPanel.add(prof);
        inputPanel.add(enlProfondeurField);
        inputPanel.add(cm);

        enlCoin1Button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ee) {
                disableAddingCoupe();
                isSelectingEnLCoin1 = true;
            }
        });

        enlCoin2Button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ee) {
                disableAddingCoupe();
                isSelectingEnLCoin2 = true;
            }
        });

        FocusAdapter enllistener = new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                Double coin1x = getValidDigitValue(enlCoin1XField);
                Double coin1y = getValidDigitValue(enlCoin1YField);
                Double coin2x = getValidDigitValue(enlCoin2XField);
                Double coin2y = getValidDigitValue(enlCoin2YField);
                Double profondeur = getValidDigitValue(enlProfondeurField);
                if (!isSwitchingPanel) {
                    try {
                        controleur.modifCoupeProfondeur(selectedElement, profondeur);
                        controleur.modifEnL(selectedElement, coin2x, coin2y, coin1x, coin1y);
                        refreshAll();
                    } catch (Exception e1) {
                        addToConsole(e1.getMessage(), Color.RED);
                    }
                }
            }
        };

        enlProfondeurField.addFocusListener(enllistener);
        enlCoin1XField.addFocusListener(enllistener);
        enlCoin1YField.addFocusListener(enllistener);
        enlCoin2XField.addFocusListener(enllistener);
        enlCoin2YField.addFocusListener(enllistener);

        JPanel panelBouton = new JPanel();
        panelBouton.setLayout(new FlowLayout(FlowLayout.CENTER));
        panelBouton.add(enlSupprimerButton);

        this.panelEnL = new JPanel();
        this.panelEnL.setLayout(new BorderLayout(5, 5));

        this.panelEnL.add(titleLabel, BorderLayout.NORTH);
        this.panelEnL.add(inputPanel, BorderLayout.CENTER);

        this.enlOutilComboBox = new JComboBox<>();

        enlOutilComboBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (enlOutilComboBox.getSelectedIndex() != -1 && selectedElement != null && !isSwitchingPanel) {
                    controleur.modifCoupeOutil(selectedElement, currentdto.getOutilsList().get(enlOutilComboBox.getSelectedIndex()).getId());
                    refreshAll();
                }
            }
        });

        JPanel outilMiniPanel = new JPanel();
        outilMiniPanel.setLayout(new BoxLayout(outilMiniPanel, BoxLayout.X_AXIS)); // Align horizontally
        outilMiniPanel.add(new JLabel("Outil :  "));
        outilMiniPanel.add(enlOutilComboBox);

        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.add(outilMiniPanel, BorderLayout.NORTH);
        bottomPanel.add(panelBouton, BorderLayout.SOUTH);

        this.panelEnL.add(bottomPanel, BorderLayout.SOUTH);
    }


    private void createOutilsPanel() {
        JLabel titleLabel = new JLabel("Outils", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 16));

        this.panelOutils = new JPanel();
        panelOutils.setLayout(new BorderLayout(5, 5));

        this.outilListe = new JList<>();
        this.outilListe.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        JScrollPane outilScrollPane = new JScrollPane(outilListe);
        outilScrollPane.setPreferredSize(new Dimension(150, 200));

        outilListe.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && !isSwitchingPanel) {
                int indexSelectionne = outilListe.getSelectedIndex();
                if (indexSelectionne != -1) {
                    OutilDTO selectedOutil = currentdto.getOutilsList().get(indexSelectionne);
                    controleur.setCurrOutil(selectedOutil.getId());
                    refreshAll();
                    outilNom.setText(currentdto.getCurrOutil().getNom());
                    outilDiametre.setText(String.format(Locale.US, "%.3f", currentdto.getCurrOutil().getDiametre()));
                    System.out.println("Outil sélectionné : " + selectedOutil.getNom());
                } else {
                    System.out.println("Aucun outil sélectionné.");
                }
            }
        });

        // Mise à jour initiale de la liste des outils

        // Panel pour les boutons sous la liste des outils
        JPanel boutonsPanel = new JPanel();
        boutonsPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
        this.addOutil = new JButton("Ajouter un outil");
        this.delOutil = new JButton("Supprimer l'outil sélectionné");

        addOutil.addActionListener(e -> {
            if (!isSwitchingPanel) {
                try {
                    controleur.addOutil("Nouvel Outil", 1);
                    refreshAll();
                    updateOutilsList(currentdto.getCurrOutil());
                } catch (Exception e1) {
                    addToConsole(e1.getMessage(), Color.RED);
                }
            }
        });

        delOutil.addActionListener(e -> {
            if (!isSwitchingPanel) {
                try {
                    int indexSelectionne = outilListe.getSelectedIndex();
                    if (indexSelectionne != -1) {
                        OutilDTO outilASupprimer = currentdto.getOutilsList().get(indexSelectionne);
                        controleur.suppOutil(outilASupprimer.getId());
                        refreshAll();
                        updateOutilsList(currentdto.getCurrOutil());
                    }
                } catch (Exception ee) {
                    addToConsole(ee.getMessage(), Color.RED);
                }
            }
        });

        boutonsPanel.add(addOutil);
        boutonsPanel.add(delOutil);

        // Panel pour les champs de texte "Nom" et "Diamètre"
        JPanel champsPanel = new JPanel();
        champsPanel.setLayout(new GridLayout(2, 2, 5, 5)); // 2 lignes, 2 colonnes pour les labels et champs
        JLabel nomLabel = new JLabel("Nom:");
        this.outilNom = new JTextField(10);
        JLabel diametreLabel = new JLabel("Diamètre:");
        this.outilDiametre = new JTextField(10);

        FocusAdapter outillistener = new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                if (isSwitchingPanel) {
                    return;
                }
                Double diametre = getValidDigitValue(outilDiametre);
                String nom = outilNom.getText();
                try {
                    controleur.modifOutil(currentdto.getCurrOutil().getId(), nom, diametre);
                    refreshAll();
                    updateOutilsList(currentdto.getCurrOutil());
                } catch (Exception e1) {
                    addToConsole(e1.getMessage(), Color.RED);
                }
            }
        };
        outilNom.addFocusListener(outillistener);
        outilDiametre.addFocusListener(outillistener);

        champsPanel.add(nomLabel);
        champsPanel.add(outilNom);
        champsPanel.add(diametreLabel);
        champsPanel.add(outilDiametre);

        // Panel inférieur pour regrouper les boutons et les champs
        JPanel basPanel = new JPanel();
        basPanel.setLayout(new BorderLayout(5, 5));
        basPanel.add(boutonsPanel, BorderLayout.NORTH); // Boutons en haut
        basPanel.add(champsPanel, BorderLayout.SOUTH);  // Champs en bas

        // Ajout des composants au panel principal
        panelOutils.add(titleLabel, BorderLayout.NORTH);     // Titre en haut
        panelOutils.add(outilScrollPane, BorderLayout.CENTER); // Liste des outils au centre
        panelOutils.add(basPanel, BorderLayout.SOUTH);       // Boutons et champs en bas
    }

    private void createZonesInterditesPanel() {
        JLabel titleLabel = new JLabel("Zones Interdites", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 16));


        panelZonesInterdites = new JPanel();
        panelZonesInterdites.setLayout(new BorderLayout(10, 10));
        panelZonesInterdites.add(titleLabel, BorderLayout.NORTH);

        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new GridLayout(2, 4, 5, 5));

        x1Label = new JLabel("X1:", SwingConstants.RIGHT);
        zoneInterditeX1Field = new JTextField(5);
        y1Label = new JLabel("Y1:", SwingConstants.RIGHT);
        zoneInterditeY1Field = new JTextField(5);

        x2Label = new JLabel("X2:", SwingConstants.RIGHT);
        zoneInterditeX2Field = new JTextField(5);
        y2Label = new JLabel("Y2:", SwingConstants.RIGHT);
        zoneInterditeY2Field = new JTextField(5);

        inputPanel.add(x1Label);
        inputPanel.add(zoneInterditeX1Field);
        inputPanel.add(y1Label);
        inputPanel.add(zoneInterditeY1Field);
        inputPanel.add(x2Label);
        inputPanel.add(zoneInterditeX2Field);
        inputPanel.add(y2Label);
        inputPanel.add(zoneInterditeY2Field);

        DefaultListModel<String> listModel = new DefaultListModel<>();
        this.zoneListe = new JList<>();
        this.zoneListe.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);


        zoneListe.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && !isSwitchingPanel) {
                int indexSelectionne = zoneListe.getSelectedIndex();
                selectedZone = null;
                if (indexSelectionne != -1) {
                    selectedZone = currentdto.getZoneListe().get(indexSelectionne).getId();
                    setSelectedElement(selectedZone);
                    updateZonesList();
                }
                refreshAll();
            }
        });

        JScrollPane zoneInterditeScrollPane = new JScrollPane(zoneListe);

        JPanel listPanel = new JPanel(new BorderLayout(10, 10));
        listPanel.add(new JLabel("Zones Interdites (Liste)", SwingConstants.CENTER), BorderLayout.NORTH);
        listPanel.add(zoneInterditeScrollPane, BorderLayout.CENTER);

        zoneAjouterButton = new JButton("Ajouter");
        zoneSupprimerButton = new JButton("Supprimer");

        zoneAjouterButton.addActionListener(e -> {
            try {
                UUID newZoneId = controleur.addZone(0, 0, 15, 15);
                setSelectedElement(newZoneId);
                selectedZone = newZoneId;
                addToConsole("Zone interdite ajoutée !", Color.BLUE);
                refreshAll();
                updateZonesList();

            } catch (Exception e1) {
                addToConsole(e1.getMessage(), Color.RED);
            }
        });

        zoneSupprimerButton.addActionListener(e -> {
            int selectedIndex = zoneListe.getSelectedIndex();
            if (selectedIndex != -1) {
                UUID zoneId = currentdto.getZoneListe().get(selectedIndex).getId();
                controleur.suppZone(zoneId);
                addToConsole("Zone interdite supprimée.", Color.BLUE);
                refreshAll();
            } else {
                addToConsole("Veuillez sélectionner une zone à supprimer !", Color.RED);
            }
        });

        FocusAdapter zoneFocusListener = new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                if (selectedZone != null && !isSwitchingPanel) {
                    Double x1 = getValidDigitValue(zoneInterditeX1Field);
                    Double y1 = getValidDigitValue(zoneInterditeY1Field);
                    Double x2 = getValidDigitValue(zoneInterditeX2Field);
                    Double y2 = getValidDigitValue(zoneInterditeY2Field);
                    if (x1 != null && y1 != null && x2 != null && y2 != null) {
                        try {
                            controleur.modifZone(selectedZone, x1, y1, x2, y2);
                            addToConsole("Zone interdite modifiée !", Color.BLUE);
                            refreshAll();
                            updateZonesList();
                        } catch (Exception ex) {
                            addToConsole(ex.getMessage(), Color.RED);
                        }
                    } else {
                        addToConsole("Veuillez entrer des coordonnées valides !", Color.RED);
                    }
                }
            }
        };

        zoneInterditeX1Field.addFocusListener(zoneFocusListener);
        zoneInterditeY1Field.addFocusListener(zoneFocusListener);
        zoneInterditeX2Field.addFocusListener(zoneFocusListener);
        zoneInterditeY2Field.addFocusListener(zoneFocusListener);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        buttonPanel.add(zoneAjouterButton);
        buttonPanel.add(zoneSupprimerButton);

        JPanel centerPanel = new JPanel(new BorderLayout(10, 10));
        centerPanel.add(inputPanel, BorderLayout.NORTH);
        centerPanel.add(listPanel, BorderLayout.CENTER);
        centerPanel.add(buttonPanel, BorderLayout.SOUTH);

        panelZonesInterdites.add(centerPanel, BorderLayout.CENTER);
    }

    private void updateZonesList() {
        isSwitchingPanel = true;
        List<ZoneInterditeDTO> listeZone = currentdto.getZoneListe();
        System.out.println(listeZone.size());
        List<String> nomZones = new ArrayList<>();
        for (ZoneInterditeDTO zone : listeZone) {
            nomZones.add("(" + zone.getPoint().getX() + ", " + zone.getPoint().getY() + " -> " + zone.getPoint().getX() + zone.getDimension().getLargeur() + ", " + zone.getPoint().getY() + zone.getDimension().getHauteur() + ")");

        }

        System.out.println(nomZones.size());

        zoneListe.setListData(nomZones.toArray(new String[0]));

        if (selectedZone != null) {
            int selectedIndex = -1;
            System.out.println("selectedZone = " + selectedZone);
            for (int i = 0; i < listeZone.size(); i++) {
                System.out.println(listeZone.get(i).getId());
                if (listeZone.get(i).getId() == selectedZone) {
                    selectedIndex = i;
                    System.out.println("zone selection trouvé");
                    break;
                }
            }
            System.out.println("Selectionne : " + selectedIndex);
            if (selectedIndex != -1) {
                ZoneInterditeDTO zoneselectionne = listeZone.get(selectedIndex);
                zoneListe.setSelectedIndex(selectedIndex);
                zoneInterditeX1Field.setText(String.format(Locale.US, "%.3f", zoneselectionne.getPoint().getX()));
                zoneInterditeY1Field.setText(String.format(Locale.US, "%.3f", zoneselectionne.getPoint().getY()));
                zoneInterditeX2Field.setText(String.format(Locale.US, "%.3f", zoneselectionne.getPoint().getX() + zoneselectionne.getDimension().getLargeur()));
                zoneInterditeY2Field.setText(String.format(Locale.US, "%.3f", zoneselectionne.getPoint().getY() + zoneselectionne.getDimension().getHauteur()));
                System.out.println("Current zone sélectionné : " + zoneselectionne.getId());
            }
        } else {
            zoneListe.clearSelection();
        }
        zoneListe.revalidate();
        zoneListe.repaint();
        isSwitchingPanel = false;
    }


    private void createPanneauPanel() {
        this.panelPanneau = new JPanel();
        this.panelPanneau.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);

        JLabel Panneau = new JLabel("Dimension du Panneau");
        this.panneauLargeurField = new JTextField(10);
        panneauLargeurField.setText("500.0");
        JLabel labellargeur = new JLabel("Largeur :");
        this.panneauHauteurField = new JTextField(10);
        panneauHauteurField.setText("500.0");
        JLabel labelhauteur = new JLabel("Hauteur :");
        this.panneauEpaisseurField = new JTextField(10);
        panneauEpaisseurField.setText("5");
        JLabel labelepaisseur = new JLabel("Épaisseur :");
        JLabel mm = new JLabel("mm");
        this.panneauAppliquerButton = new JButton("Appliquer");


        panneauAppliquerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ee) {
                if (isSwitchingPanel) {
                    return;
                }
                Double largeur = getValidDigitValue(panneauLargeurField);
                Double hauteur = getValidDigitValue(panneauHauteurField);
                Double epaisseur = getValidDigitValue(panneauEpaisseurField);
                if (largeur != null && hauteur != null && epaisseur != null) {
                    try {
                        controleur.addPanneau(largeur, hauteur, epaisseur);
                        addToConsole("Panneau ajouté !", Color.GREEN);
                        refreshAll();
                        disableAddingCoupe();
                    } catch (Exception e) {
                        addToConsole(e.getMessage(), Color.RED);
                        disableAddingCoupe();
                    }
                } else {
                    addToConsole("Impossible d'ajouter le panneau (données manquantes)", Color.RED);
                }
            }
        });


        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 3;
        gbc.anchor = GridBagConstraints.CENTER;
        this.panelPanneau.add(Panneau, gbc);

        gbc.gridy = 1;
        gbc.gridwidth = 1;
        gbc.anchor = GridBagConstraints.WEST;
        this.panelPanneau.add(labellargeur, gbc);

        gbc.gridx = 1;
        this.panelPanneau.add(panneauLargeurField, gbc);

        gbc.gridx = 2;
        this.panelPanneau.add(new JLabel("mm"), gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        this.panelPanneau.add(labelhauteur, gbc);

        gbc.gridx = 1;
        this.panelPanneau.add(panneauHauteurField, gbc);

        gbc.gridx = 2;
        this.panelPanneau.add(new JLabel("mm"), gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        this.panelPanneau.add(labelepaisseur, gbc);

        gbc.gridx = 1;
        this.panelPanneau.add(panneauEpaisseurField, gbc);

        gbc.gridx = 2;
        this.panelPanneau.add(new JLabel("mm"), gbc);

        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 3;
        gbc.anchor = GridBagConstraints.CENTER;
        this.panelPanneau.add(panneauAppliquerButton, gbc);


    }

    private void clearDynamicPanel() {
        updateDynamicPanel(new JPanel());
    }

    private Double getValidDigitValue(JTextField textField1) {
        try {
            return Double.parseDouble(textField1.getText());
        } catch (NumberFormatException e) {
            textField1.setText("");
            return null;
        }
    }

    private void updateOutilComboBox(JComboBox<String> comboBox, OutilDTO outil) {
        List<OutilDTO> outilsList = currentdto.getOutilsList();
        comboBox.setModel(new DefaultComboBoxModel<>(
                outilsList.stream().map(OutilDTO::getNom).toArray(String[]::new)
        ));

        int selectedIndex = 0;
        if (outil != null) {
            selectedIndex = -1;
            for (int i = 0; i < outilsList.size(); i++) {
                if (outilsList.get(i).getId() == outil.getId()) {
                    selectedIndex = i;
                    break;
                }
            }
        }

        if (selectedIndex >= 0 && selectedIndex < comboBox.getItemCount()) {
            comboBox.setSelectedIndex(selectedIndex);
        } else {
            comboBox.setSelectedIndex(-1);
        }
    }
}
