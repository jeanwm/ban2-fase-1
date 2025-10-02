// Arquivo: TelefoneCRUD.java
import java.sql.*;
import java.util.Scanner;

public class TelefoneCRUD {
    private Connection connection;
    private Scanner scanner;
    
    public TelefoneCRUD(Connection connection, Scanner scanner) {
        this.connection = connection;
        this.scanner = scanner;
    }
    
    public void menu() throws SQLException {
        while (true) {
            System.out.println("\n=== TELEFONES ===");
            System.out.println("1. Cadastrar");
            System.out.println("2. Listar");
            System.out.println("3. Atualizar");
            System.out.println("4. Deletar");
            System.out.println("5. Voltar");
            System.out.print("Escolha: ");
            
            int opcao = scanner.nextInt();
            scanner.nextLine();
            
            switch (opcao) {
                case 1 -> cadastrar();
                case 2 -> listar();
                case 3 -> atualizar();
                case 4 -> deletar();
                case 5 -> { return; }
                default -> System.out.println("Opção inválida!");
            }
        }
    }
    
    private void cadastrar() throws SQLException {
        System.out.print("Número: ");
        int numero = scanner.nextInt();
        
        String sql = "INSERT INTO telefones (numero) VALUES (?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, numero);
            stmt.executeUpdate();
            System.out.println("Telefone cadastrado com sucesso!");
        }
    }
    
    private void listar() throws SQLException {
        String sql = "SELECT * FROM beneficios";
        try (Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                System.out.printf("ID: %d | Nome: %s | Descrição: %s\n",
                    rs.getInt("id_beneficio"),
                    rs.getString("nome"),
                    rs.getString("descricao"));
            }
        }
    }
    
    private void atualizar() throws SQLException {
        System.out.print("ID do benefício a atualizar: ");
        int id = scanner.nextInt();
        scanner.nextLine();
        System.out.print("Novo nome: ");
        String nome = scanner.nextLine();
        System.out.print("Nova descrição: ");
        String descricao = scanner.nextLine();
        
        String sql = "UPDATE beneficios SET nome = ?, descricao = ? WHERE id_beneficio = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, nome);
            stmt.setString(2, descricao);
            stmt.setInt(3, id);
            int affectedRows = stmt.executeUpdate();

            if (affectedRows > 0) {
                System.out.println("Benefício atualizado com sucesso!");
            } else {
                System.out.println("Benefício não encontrado!");
            }
        }
    }
    
    private void deletar() throws SQLException {
        System.out.print("ID do benefício a deletar: ");
        int id = scanner.nextInt();
        scanner.nextLine();
        
        String sql = "DELETE FROM beneficios WHERE id_beneficio = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            int affectedRows = stmt.executeUpdate();
            
            if (affectedRows > 0) {
                System.out.println("Benefício deletado com sucesso!");
            } else {
                System.out.println("Benefício não encontrado!");
            }
        }
    }
}