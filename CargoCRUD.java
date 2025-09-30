// Arquivo: CargoCRUD.java
import java.sql.*;
import java.util.Scanner;

public class CargoCRUD {
    private Connection connection;
    private Scanner scanner;
    
    public CargoCRUD(Connection connection, Scanner scanner) {
        this.connection = connection;
        this.scanner = scanner;
    }
    
    public void menu() throws SQLException {
        while (true) {
            System.out.println("\n=== CARGOS ===");
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
        System.out.print("Nome: ");
        String nome = scanner.nextLine();
        System.out.print("Descrição: ");
        String descricao = scanner.nextLine();
        System.out.print("Salário: ");
        float salario = scanner.nextFloat();
        
        String sql = "INSERT INTO cargos (nome, descricao, salario) VALUES (?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, nome);
            stmt.setString(2, descricao);
            stmt.setFloat(3, salario);
            stmt.executeUpdate();
            System.out.println("Cargo cadastrado com sucesso!");
        }
    }
    
    private void listar() throws SQLException {
        String sql = "SELECT * FROM cargos";
        try (Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                System.out.printf("ID: %d | Nome: %s | Descrição: %s | Salário: %f\n",
                    rs.getInt("id_cargo"),
                    rs.getString("nome"),
                    rs.getString("descricao"),
                    rs.getFloat("salario"));
            }
        }
    }
    
    private void atualizar() throws SQLException {
        System.out.print("ID do cargo a atualizar: ");
        int id = scanner.nextInt();
        scanner.nextLine();
        System.out.print("Novo nome: ");
        String nome = scanner.nextLine();
        System.out.print("Nova descrição: ");
        String descricao = scanner.nextLine();
        System.out.print("Novo salário: ");
        float salario = scanner.nextFloat();
        
        String sql = "UPDATE cargos SET nome = ?, descricao = ?, salario = ? WHERE id_cargo = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, nome);
            stmt.setString(2, descricao);
            stmt.setFloat(3, salario);
            stmt.setInt(4, id);
            int affectedRows = stmt.executeUpdate();

            if (affectedRows > 0) {
                System.out.println("Cargo atualizado com sucesso!");
            } else {
                System.out.println("Cargo não encontrado!");
            }
        }
    }
    
    private void deletar() throws SQLException {
        System.out.print("ID do cargo a deletar: ");
        int id = scanner.nextInt();
        scanner.nextLine();
        
        String sql = "DELETE FROM cargos WHERE id_cargo = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            int affectedRows = stmt.executeUpdate();

            if (affectedRows > 0) {
                System.out.println("Cargo deletado com sucesso!");
            } else {
                System.out.println("Cargo não encontrado!");
            }
        }
    }
}