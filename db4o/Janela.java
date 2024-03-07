package projeto.db4o;
import com.db4o.Db4o;
import com.db4o.Db4oEmbedded;
import com.db4o.ObjectContainer;
import com.db4o.ObjectSet;
import java.awt.event.ActionEvent;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;


public class Janela extends JFrame {
    
   // Chamando o aqrquivo Fun
    Fun fun = new Fun();
    
    // Banco de dados
    private ObjectContainer db;
    ObjectSet<Fun> lista;
    
   // Variaveis do Formulario
    private JTextField txtPesquisa, txtNome, txtTelefone, txtEndereco;
    private JTable tabela;
    private DefaultTableModel tableModel;
    public Janela(){
        //font
        
        // janela
        setTitle("Cadastro");
        setSize(827,400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        setLocationRelativeTo(null);
        setLayout(null);
        
        // Formulario Buscar
        JLabel txtPesquisal = new JLabel("Pesquisar: ");
        txtPesquisal.setBounds(5,0,204,40);
        txtPesquisa = new JTextField();
        txtPesquisa.setBounds(1,30,225,40);
        add(txtPesquisa);
        add(txtPesquisal);
        
        // Formulario NOME
        JLabel txtNomel = new JLabel("Nome: ");
        txtNomel.setBounds(5,102,204,40);
        txtNome = new JTextField();
        txtNome.setBounds(70,106,238,40);
        add(txtNome);
        add(txtNomel);
        
        // Formulario TELEFONE
        JLabel txtTelefonel = new JLabel("Teçefone: ");
        txtTelefonel.setBounds(5,152,204,40);
        txtTelefone = new JTextField( );
        txtTelefone.setBounds(70,156,238,40);
        add(txtTelefone);
        add(txtTelefonel);
         
        // Formulario ENDEREÇO
        JLabel txtEnderecol = new JLabel("Endereço: ");
        txtEnderecol.setBounds(5,202,204,40);
        txtEndereco = new JTextField(" ");
        txtEndereco.setBounds(70,206,238,40);
        add(txtEndereco);
        add(txtEnderecol);
        
        // Botão Buscar
        JButton btnBuscar = new JButton("🔎");
        btnBuscar.setBounds(226, 30, 80, 40);
        btnBuscar.addActionListener(this::Buscar);
        add(btnBuscar);
        
        // Botão Inserir
        JButton btnInserir = new JButton("Inserir");
        btnInserir.setBounds(2, 288, 100, 70);
        btnInserir.addActionListener(this::Inserir);
        add(btnInserir);

        // Botão Atualizar
        JButton btnAtualizar = new JButton("Atualizar");
        btnAtualizar.setBounds(206, 288, 100, 70);
        btnAtualizar.addActionListener(this::Atualizar);
        add(btnAtualizar);

        // Botão Deletar
        JButton btnDeletar = new JButton("Deletar");
        btnDeletar.setBounds(104, 288, 100, 70);
        btnDeletar.addActionListener(this::Deletar);
        add(btnDeletar);
        
        
        // Tabela
        JPanel tabelaPanel = new JPanel(new BorderLayout());
        tabelaPanel.setBounds(310, 30, 500, 330);
        add(tabelaPanel);

        // Cabeçalhos da tabela
        String[] columnNames = new String[]{"Nome", "Telefone", "Endereço"};

        // Modelo da tabela
        tableModel = new DefaultTableModel(null, columnNames);
        tabela = new JTable(tableModel);

        // Adiciona a tabela a um JScrollPane para rolar
        JScrollPane scrollPane = new JScrollPane(tabela);
        tabelaPanel.add(scrollPane, BorderLayout.CENTER);

        // Atualiza a tabela
        atualizarTabela();

        setVisible(true);
   }
   // Vaiaveis em texto
   public String nomeParaBuscar, Nome, Telefone, Endereco, novoNome,novoTelefone,novoEndereco,nomeParaAtualizar,nomeParaDeletar;
   
   // Função de inserir
   private void Inserir(ActionEvent e){
   
    // Transformando as variaves em texto 
    Nome = txtNome.getText();
    Telefone = txtTelefone.getText();   
    Endereco = txtEndereco.getText();
    
    // Verificador
    switch(JOptionPane.showConfirmDialog(null,"Confirme o seu formulario: \n\n" +
            "Nome:        " + Nome + "\n"+
            "Telefone:   " + Telefone + "\n"+
            "Endereço: " + Endereco + "\n\n"+
            "Os Dados estão corretos?", "Confimador",JOptionPane.YES_NO_OPTION, 1)){
        case 0:
    // Setando os valores e salvando no banco de dados
    
    db = Db4o.openFile("Banco.dbo");
    fun.setNome(Nome);
    fun.setTelefone(Telefone);
    fun.setEndereco(Endereco);
    db.store(fun);
    db.close();
    atualizarTabela();
    break;
    // Negando os valores
        case 1:
            JOptionPane.showMessageDialog(null,"Dados Não inseridos","Aviso",2);
    break;}
       
    }
 
   // Função de atalizar
   private void Atualizar(ActionEvent e) {
    nomeParaAtualizar = JOptionPane.showInputDialog("Digite o nome a ser atualizado:");

    if (nomeParaAtualizar != null && !nomeParaAtualizar.isEmpty()) {

        fun.setNome(nomeParaAtualizar);

        // Consulte o banco de dados
        ObjectSet<Fun> resultados = db.queryByExample(fun);

        if (resultados.hasNext()) {
            // Se encontrado, obtenha o objeto para atualização
            Fun objetoParaAtualizar = resultados.next();

            // Solicite as novas informações
            novoNome = JOptionPane.showInputDialog("Digite o novo nome\n (ou pressione Enter para manter o antigo):");
            novoTelefone = JOptionPane.showInputDialog("Digite o novo telefone\n (ou pressione Enter para manter o antigo):");
            novoEndereco = JOptionPane.showInputDialog("Digite o novo endereço\n (ou pressione Enter para manter o antigo):");

            // Atualize as informações se o usuário fornecer novos valores
            if (novoNome != null && !novoNome.isEmpty()) {
                objetoParaAtualizar.setNome(novoNome);
            }
            if (novoTelefone != null && !novoTelefone.isEmpty()) {
                objetoParaAtualizar.setTelefone(novoTelefone);
            }
            if (novoEndereco != null && !novoEndereco.isEmpty()) {
                objetoParaAtualizar.setEndereco(novoEndereco);
            }

            // Atualize o objeto no banco de dados
            db.store(objetoParaAtualizar);
            db.commit();

            JOptionPane.showMessageDialog(null, "Registro atualizado com sucesso", "Aviso", JOptionPane.INFORMATION_MESSAGE);
            
            atualizarTabela();
        } else {
            JOptionPane.showMessageDialog(null, "Registro não encontrado", "Aviso", JOptionPane.WARNING_MESSAGE);
        }
    } else {
        JOptionPane.showMessageDialog(null, "Nome inválido", "Aviso", JOptionPane.WARNING_MESSAGE);
    }
}
   
   // Função de deletar
   private void Deletar(ActionEvent e) {
    nomeParaDeletar = JOptionPane.showInputDialog("Digite o nome a ser deletado:");

    if (nomeParaDeletar != null && !nomeParaDeletar.isEmpty()) {
        ObjectContainer db = null;

        try {
            // Abra o banco de dados
            db = Db4oEmbedded.openFile(Db4oEmbedded.newConfiguration(), "Banco.dbo");

            fun.setNome(nomeParaDeletar);

            // Consulta no banco de dados para encontrar o objeto com o mesmo nome
            ObjectSet<Fun> resultados = db.queryByExample(fun);

            if (resultados.hasNext()) {
                // Se encontrado, delete o objeto do banco de dados
                Fun objParaDeletar = resultados.next();
                db.delete(objParaDeletar);
                db.commit();
                JOptionPane.showMessageDialog(null, "Registro deletado com sucesso", "Aviso", JOptionPane.INFORMATION_MESSAGE);
                atualizarTabela();
            } else {
                JOptionPane.showMessageDialog(null, "Registro não encontrado", "Aviso", JOptionPane.WARNING_MESSAGE);
            }
        } finally {
      
            if (db != null) {
                db.close();
            }
        }
    } else {
        JOptionPane.showMessageDialog(null, "Nome inválido", "Aviso", JOptionPane.WARNING_MESSAGE);} }
   
   // Função de Buscar
   private void Buscar(ActionEvent e) {
    nomeParaBuscar = txtPesquisa.getText();

    if (nomeParaBuscar != null && !nomeParaBuscar.isEmpty()) {
        ObjectContainer db = null;

        try {
            // Abra o banco de dados
            db = Db4oEmbedded.openFile(Db4oEmbedded.newConfiguration(), "Banco.dbo");

            fun.setNome(nomeParaBuscar);

            // Consulte o banco de dados para encontrar o objeto com o mesmo nome
            ObjectSet<Fun> resultados = db.queryByExample(fun);

            if (resultados.hasNext()) {
                // Se encontrado, exiba os dados do registro
                Fun objetoEncontrado = resultados.next();
                JOptionPane.showMessageDialog(null, "Registro encontrado:\n" +
                        "Nome: " + objetoEncontrado.getNome() + "\n" +
                        "Telefone: " + objetoEncontrado.getTelefone() + "\n" +
                        "Endereço: " + objetoEncontrado.getEndereco(), "Resultado da Busca", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(null, "Registro não encontrado", "Aviso", JOptionPane.WARNING_MESSAGE);
            }
        } finally {
            // Certifique-se de fechar o banco de dados, mesmo em caso de exceção
            if (db != null) {
                db.close();
            }
        }
    } else {
        JOptionPane.showMessageDialog(null, "Nome inválido", "Aviso", JOptionPane.WARNING_MESSAGE);
    }
}
   // Função para atualizar a tabela
   private void atualizarTabela() {
        // Limpa a tabela antes de atualizar
        tableModel.setRowCount(0);

        // Recupera os dados do banco de dados e atualiza a tabela
        db = Db4oEmbedded.openFile(Db4oEmbedded.newConfiguration(), "Banco.dbo");
        lista = db.query(Fun.class);

        while (lista.hasNext()) {
            Fun fun = lista.next();
            // Adiciona uma nova linha na tabela com os dados do objeto Fun
            tableModel.addRow(new Object[]{fun.getNome(), fun.getTelefone(), fun.getEndereco()});
        }

        db.close();
    }
}