import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

import java.awt.*;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import entities.Contato;

public class AgendaSwing extends JFrame {

    private DefaultListModel<Contato> listaModel;
    private JList<Contato> listaContatos;
    private JTextField campoNome, campoTelefone;
    private static final String ARQUIVO = "contatos.txt";

    public AgendaSwing() {
        super("Agenda de Contatos");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(600, 600);
        setLayout(new BorderLayout());

        // Lista de contatos
        listaModel = new DefaultListModel<>();
        listaContatos = new JList<>(listaModel);
        add(new JScrollPane(listaContatos), BorderLayout.CENTER);

        // Formulário de entrada
        JPanel form = new JPanel(new GridLayout(2, 2));
        form.add(new JLabel("Nome:"));
        campoNome = new JTextField();
        form.add(campoNome);

        form.add(new JLabel("Telefone: (somente números)"));
        campoTelefone = new JTextField();
        form.add(campoTelefone);

        add(form, BorderLayout.NORTH);

        //Botões
        JPanel botoes = new JPanel();
        JButton btnAdicionar = new JButton("Adicionar");
        JButton btnEditar = new JButton("Editar");
        JButton btnRemover = new JButton("Remover");
        JButton btnSalvar = new JButton("Salvar");
        JButton btnCarregar = new JButton("Carregar");

        botoes.add(btnAdicionar);
        botoes.add(btnEditar);
        botoes.add(btnRemover);
        botoes.add(btnSalvar);
        botoes.add(btnCarregar);

        add(botoes, BorderLayout.SOUTH);

        // Ação dos botões
        btnAdicionar.addActionListener(e -> {
            String nome = campoNome.getText();
            String telefoneStr = campoTelefone.getText();

            try {
                long telefone = Long.parseLong(telefoneStr);

                if (!nome.isEmpty()) {
                    listaModel.addElement(new Contato(nome, telefone));
                    campoNome.setText("");
                    campoTelefone.setText("");

                } else {
                    JOptionPane.showMessageDialog(this, "Digite um nome válido.");
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "O telefone deve conter apenas números.");
            }

        });

        btnEditar.addActionListener(e -> {
            int index = listaContatos.getSelectedIndex();
            if (index != -1) {
                Contato contato = listaModel.getElementAt(index);
                String novoNumeroTelefone = JOptionPane.showInputDialog(this, "Novo número de telefone",
                        contato.getTelefone());

                try {
                    long novoTelefone = Long.parseLong(novoNumeroTelefone);
                    contato.setTelefone(novoTelefone);
                    listaContatos.repaint();
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(this, "O telefone deve conter apenas números.");
                }
            }
            ;
        });

        btnRemover.addActionListener(e -> {
            int index = listaContatos.getSelectedIndex();

            if (index != -1) {
                listaModel.remove(index);
            }
        });

        btnSalvar.addActionListener(e -> {

            salvarContatos();
        });

        btnCarregar.addActionListener(e -> {
            carregarContatos();
        });

        setVisible(true);

        //carregarContatos();
    }
    
    private void salvarContatos() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(ARQUIVO))) {
            for (int i = 0; i < listaModel.size(); i++) {
                Contato contato = listaModel.getElementAt(i);
                writer.write(contato.getNome() + ";" + contato.getTelefone());
                writer.newLine();
            }
            JOptionPane.showMessageDialog(this, "Contatos salvos em: " + ARQUIVO);
            System.out.println("Contatos salvos em: " + ARQUIVO);
        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Erro ao salvar contatos: " + e.getMessage());
        }
    }
    
    private void carregarContatos() {
        listaModel.clear();
        File file = new File(ARQUIVO);

        if (!file.exists()) {
            JOptionPane.showMessageDialog(this, "Arquivo não encontrado" + file.getAbsolutePath());
            System.out.println("Arquivo não encontrado" + file.getAbsolutePath());
            return;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String linha;
            while ((linha = reader.readLine()) != null) {
                System.out.println("Lendo linha:" + linha);
                String[] partes = linha.split(";");
                if(partes.length == 2) {
                    String nome = partes[0].trim();
                    try {
                        long telefone = Long.parseLong(partes[1].trim());
                        listaModel.addElement(new Contato(nome, telefone));
                    } catch (NumberFormatException e) {
                        JOptionPane.showMessageDialog(this, "Linha inválida: " + linha);
                    }
                } else {
                    JOptionPane.showMessageDialog(this, "Linha inválida: " + linha);
                }
            }
            JOptionPane.showMessageDialog(this, "Contatos carregados de: " + file.getAbsolutePath());
        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Erro ao carregar contatos: " + e.getMessage());

        }
    }
 

    public static void main(String[] args) {
        SwingUtilities.invokeLater(AgendaSwing::new);
    }

}
