// Arquivo: FuncionarioCRUD.java
import java.sql.*;
import java.util.Scanner;

public class FuncionarioCRUD {
    private Connection connection;
    private Scanner scanner;
    
    public FuncionarioCRUD(Connection connection, Scanner scanner) {
        this.connection = connection;
        this.scanner = scanner;
    }
    
    public void menu() throws SQLException {
        while (true) {
            System.out.println("\n=== FUNCIONÁRIOS ===");
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
        System.out.print("Data de nascimento: ");
        String data_nascimento = scanner.nextLine();
        System.out.print("Data de adesão: ");
        String data_adesao = scanner.nextLine();
        System.out.print("Data de demissão: ");
        String data_demissao = scanner.nextLine();
        System.out.print("Status: ");
        int status = scanner.nextInt();
        System.out.print("Telefone: ");
        String telefone = scanner.nextLine();
        System.out.print("Cargo: ");
        int id_cargo = scanner.nextInt();

        String sqlInsertTelefone = "INSERT INTO telefones (numero) VALUES (?)";
        try (PreparedStatement stmt = connection.prepareStatement(sqlInsertTelefone)) {
            stmt.setString(1, telefone);
            stmt.executeUpdate();

            ResultSet id_telefone = stmt.getGeneratedKeys();
        }
        
        String sql = "INSERT INTO clientes (nome, data_nascimento, data_adesao, data_demissao, status, id_telefone, id_cargo) VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, nome);
            stmt.setString(2, data_nascimento);
            stmt.setString(3, data_adesao);
            stmt.setInt(4, status);
            // stmt.setInt(5, id_telefone);
            stmt.setInt(6, id_cargo);
            stmt.executeUpdate();

            System.out.println("Funcionário cadastrado com sucesso!");
        }
    }
    
    private void listar() throws SQLException {
        String sql = "SELECT * FROM funcionarios";

        try (Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                System.out.printf("ID: %d | Nome: %s | Data de nascimento: %s | Data de adesão: %s | Data de demissão: %s | Status: %d | Telefone: %s | Cargo: %s\n",
                    rs.getInt("id_cliente"),
                    rs.getString("nome"),
                    rs.getString("data_nascimento"),
                    rs.getString("data_adesao"),
                    rs.getInt("status"),
                    rs.getString("telefone"),
                    rs.getString("cargo"));
            }
        }
    }
    
    private void atualizar() throws SQLException {
        System.out.print("ID do funcionário a atualizar: ");
        int id = scanner.nextInt();
        scanner.nextLine();
        System.out.print("Novo nome: ");
        String nome = scanner.nextLine();
        System.out.print("Nova data de nascimento: ");
        String data_nascimento = scanner.nextLine();
        System.out.print("Novo data de adesão: ");
        String data_adesao = scanner.nextLine();
        System.out.print("Novo data de demissão: ");
        String data_demissao = scanner.nextLine();
        System.out.print("Novo status: ");
        int status = scanner.nextInt();
        
        String sql = "UPDATE funcionarios SET nome = ?, data_nascimento = ?, data_adesao = ?, data_demissao = ?, status = ? WHERE id_cliente = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, nome);
            stmt.setString(2, data_nascimento);
            stmt.setString(3, data_adesao);
            stmt.setString(3, data_demissao);
            stmt.setInt(4, status);
            stmt.setInt(6, id);
            int affectedRows = stmt.executeUpdate();

            if (affectedRows > 0) {
                System.out.println("Funcionário atualizado com sucesso!");
            } else {
                System.out.println("Funcionário não encontrado!");
            }
        }
    }
    
    private void deletar() throws SQLException {
        System.out.print("ID do funcionário a deletar: ");
        int id = scanner.nextInt();
        scanner.nextLine();
        
        String sql = "DELETE FROM funcionarios WHERE id_funcionarios = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            int affectedRows = stmt.executeUpdate();

            if (affectedRows > 0) {
                System.out.println("Funcionário deletado com sucesso!");
            } else {
                System.out.println("Funcionário não encontrado!");
            }
        }
    }
} 