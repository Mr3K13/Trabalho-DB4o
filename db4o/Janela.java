package projeto.db4o;
import com.db4o.Db4o;
import com.db4o.Db4oEmbedded;
import com.db4o.ObjectContainer;
import com.db4o.ObjectSet;
import com.db4o.query.Predicate;
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
        JLabel txtTelefonel = new JLabel("Telefone: ");
        txtTelefonel.setBounds(5,152,204,40);
        txtTelefone = new JTextField();
        txtTelefone.setBounds(70,156,238,40);
        add(txtTelefone);
        add(txtTelefonel);
         
        // Formulario ENDEREÇO
        JLabel txtEnderecol = new JLabel("Endereço: ");
        txtEnderecol.setBounds(5,202,204,40);
        txtEndereco = new JTextField();
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
        tableModel = new DefaultTableModel(null, columnNames) {
            
        // Sobrescreve o método para tornar as células não editáveis
        @Override
        
        public boolean isCellEditable(int row, int column) {
            
            return false;
       }
   };
        tabela = new JTable(tableModel);
        
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
    
    
     // Verificador de valor de estado de variavel
    if (Nome.isEmpty() || Telefone.isEmpty() || Endereco.isEmpty()) {
        JOptionPane.showMessageDialog(null, "Por favor, preencha todos os campos obrigatórios!", "Aviso", 2);
    }
        else if(!isNumberic(Telefone)){
         JOptionPane.showMessageDialog(null, "Por favor, insira somente números no campo Telefone!", "Aviso", 2);
    }
    
    else{
    // Verificador
    switch(JOptionPane.showConfirmDialog(null,"Confirme o seu formulário: \n\n" +
            "Nome:        " + Nome + "\n"+
            "Telefone:   " + Telefone + "\n"+
            "Endereço: " + Endereco + "\n\n"+
            "Os Dados estão corretos?", "Confimador",JOptionPane.YES_NO_OPTION, 1)){
        case 0 : 
            
            // Setando os valores e salvando no banco de dados 
            db = Db4o.openFile("Banco.dbo");
            fun.setNome(Nome);
            fun.setTelefone(Telefone);
            fun.setEndereco(Endereco);
            db.store(fun);
            db.close();
            
            JOptionPane.showMessageDialog(null,"Dados inseridos","Aviso",1);
            
            atualizarTabela();
            break;
            
        case 1 :
            JOptionPane.showMessageDialog(null,"Dados Não inseridos","Aviso",2);
        break;
}
   
     }
}
 
   // Função de atalizar
   private void Atualizar(ActionEvent e) {
     
    nomeParaAtualizar = JOptionPane.showInputDialog("Digite o nome a ser atualizado:");

    if (nomeParaAtualizar != null && !nomeParaAtualizar.isEmpty()) {
        ObjectContainer db = null;

        try {
            // Abra o banco de dados
            db = Db4oEmbedded.openFile(Db4oEmbedded.newConfiguration(), "Banco.dbo");
            
           // Convertendo o nome para minúsculas
            String nomeLowerCase = nomeParaAtualizar.toLowerCase();

            // Consulta no banco de dados para encontrar o objeto com o mesmo nome (em minúsculas)
            ObjectSet<Fun> resultados = db.query(new Predicate<Fun>() {
                @Override
                public boolean match(Fun obj) {
                    return obj.getNome().toLowerCase().equals(nomeLowerCase);
                }
            });
            if (resultados.hasNext()) {
         
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
                db.close();
                JOptionPane.showMessageDialog(null, "Registro atualizado com sucesso", "Aviso", 1);

                atualizarTabela();
            } else {
                JOptionPane.showMessageDialog(null, "Registro não encontrado\n\n   Coloque um NOME valido", "Aviso", 2);
            }
        } finally {
            // Certifique-se de fechar o banco de dados, mesmo em caso de exceção
            if (db != null) {
                db.close();
            }
        }
    } else {
        JOptionPane.showMessageDialog(null,"Dados Não inseridos\n\n   Coloque um NOME valido", "Aviso", 2);
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

            // Convertendo o nome para minúsculas
            String nomeLowerCase = nomeParaDeletar.toLowerCase();

            // Consulta no banco de dados para encontrar o objeto com o mesmo nome (em minúsculas)
            ObjectSet<Fun> resultados = db.query(new Predicate<Fun>() {
                @Override
                public boolean match(Fun obj) {
                    return obj.getNome().toLowerCase().equals(nomeLowerCase);
                }
            });

            if (resultados.hasNext()) {
                Fun objParaDeletar = resultados.next();
               switch(JOptionPane.showConfirmDialog(null,"Confirme o formulario a ser deletado: \n\n" +
            "Nome:        " + objParaDeletar.getNome() + "\n"+
            "Telefone:   " + objParaDeletar.getTelefone() + "\n"+
            "Endereço: " + objParaDeletar.getEndereco()+ "\n\n"+
            "Os Dados estão corretos?", "Confimador",JOptionPane.YES_NO_OPTION, 1)){
            case 0:
            
            // Se encontrado, delete o objeto do banco de dados
                db.delete(objParaDeletar);
                db.commit();
                db.close();
                JOptionPane.showMessageDialog(null, "Registro deletado com sucesso", "Aviso", 1);
                atualizarTabela();
                break;
   
        // Negando os valores
           case 1 : 
            
            JOptionPane.showMessageDialog(null,"Dados Não Deletados","Aviso",2);
        
            break;
}
            } else {
                JOptionPane.showMessageDialog(null, "Registro não encontrado\n\n   Coloque um NOME valido", "Aviso", 2);
            }
        } finally {
      
            if (db != null) {
                db.close();
            }
        }
    } else {
        JOptionPane.showMessageDialog(null, "Dados Não inseridos\n\n   Coloque um NOME valido", "Aviso", 2);} }
   
   // Função de Buscar
   private void Buscar(ActionEvent e) {
    nomeParaBuscar = txtPesquisa.getText();

    if (nomeParaBuscar != null && !nomeParaBuscar.isEmpty()) {
        ObjectContainer db = null;

        try {
            // Abra o banco de dados
            db = Db4oEmbedded.openFile(Db4oEmbedded.newConfiguration(), "Banco.dbo");

            // Convertendo o nome para minúsculas
            String nomeLowerCase = nomeParaBuscar.toLowerCase();

            // Consulte o banco de dados para encontrar o objeto
            ObjectSet<Fun> resultados = db.query (new Predicate<Fun>() {
                
                @Override
                public boolean match(Fun obj) {
                    return obj.getNome().toLowerCase().equals(nomeLowerCase);
                }
            });

            if (resultados.hasNext()) {
                // Se encontrado, exiba os dados do registro
                Fun objetoEncontrado = resultados.next();
                JOptionPane.showMessageDialog(null, "Registro encontrado:\n" +
                        "Nome: " + objetoEncontrado.getNome() + "\n" +
                        "Telefone: " + objetoEncontrado.getTelefone() + "\n" +
                        "Endereço: " + objetoEncontrado.getEndereco(), "Resultado da Busca", 1);
            } else {
                JOptionPane.showMessageDialog(null, "Registro não encontrado", "Aviso", 2);
            }
        } finally {
            // Certifique-se de fechar o banco de dados, mesmo em caso de exceção
            if (db != null) {
                db.close();
            }
        }
    } else {
        JOptionPane.showMessageDialog(null, "Nome inválido", "Aviso", 2);
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
   
   // Função para a caixa de telefone aceitar só numero
   private boolean isNumberic(String str) {
         if (str == null) return false; // handle null-pointer
        if (str.length() == 0) return false; // handle empty strings
        // to make sure that the input is numeric, we have to go through the characters
        for (char c : str.toCharArray()) {
            if (!Character.isDigit(c)) return false; // we found a non-digit character so we can early return
        }
        return true;
    }
}