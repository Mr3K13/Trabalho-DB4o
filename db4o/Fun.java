package projeto.db4o;

public class Fun {
    //Banco de dados

    
    // Variaveis dos valoes
    private String Pesquisa, Nome, Telefone, Endereco;

    
    // Recebendo os valores
    public void setPesquisa(String _Pesquisa){Pesquisa = _Pesquisa;}
    public void setNome(String _Nome){Nome = _Nome;}
    public void setTelefone(String _Telefone){Telefone = _Telefone;}
    public void setEndereco(String _Endereco){Endereco = _Endereco;}
    
    // Devolvendo os valores
    public String getPesqusa(){return Pesquisa;}
    public String getNome(){return Nome;}
    public String getTelefone(){return Telefone;}
    public String getEndereco(){return Endereco;}

}
