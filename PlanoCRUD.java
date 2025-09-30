// Arquivo: PlanoCRUD.java
import java.sql.*;
import java.util.Scanner;

public class PlanoCRUD {
    private Connection connection;
    private Scanner scanner;
    
    public PlanoCRUD(Connection connection, Scanner scanner) {
        this.connection = connection;
        this.scanner    = scanner;
    }
    
    public void menu() throws SQLException {
        while (true) {
            System.out.println("\n=== PLANOS ===");
            System.out.println("1. Cadastrar");
            System.out.println("2. Listar");
            System.out.println("3. Atualizar");
            System.out.println("4. Deletar");
            System.out.println("5. Vincular Benefício");
            System.out.println("6. Voltar");
            System.out.print("Escolha: ");
            
            int opcao = scanner.nextInt();
            scanner.nextLine();
            
            switch (opcao) {
                case 1 -> cadastrar();
                case 2 -> listar();
                case 3 -> atualizar();
                case 4 -> deletar();
                case 5 -> vincular();
                case 6 -> { return; }
                default -> System.out.println("Opção inválida!");
            }
        }
    }
    
    private void cadastrar() throws SQLException {
        System.out.print("Nome: ");
        String nome = scanner.nextLine();
        System.out.print("Descrição: ");
        String descricao = scanner.nextLine();
        System.out.print("Valor: ");
        float valor = scanner.nextFloat();
        System.out.print("Tempo de duração: ");
        int duracao = scanner.nextInt();
        System.out.print("Tempo de fidelidade: ");
        int fidelidade = scanner.nextInt();
        
        String sql = "INSERT INTO planos (nome, descricao, valor, duracao, fidelidade) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, nome);
            stmt.setString(2, descricao);
            stmt.setFloat(3, valor);
            stmt.setInt(4, duracao);
            stmt.setInt(5, fidelidade);
            stmt.executeUpdate();

            System.out.println("Plano cadastrado com sucesso!");
        }
    }
    
    private void listar() throws SQLException {
        String sql = "SELECT * FROM planos";

        try (Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                System.out.printf("ID: %d | Nome: %s | Descrição: %s | Valor: %.2f | Tempo de duração: %s | Tempo de fidelidade: %s\n",
                    rs.getInt("id_plano"),
                    rs.getString("nome"),
                    rs.getString("descricao"),
                    rs.getFloat("valor"),
                    rs.getInt("duracao"),
                    rs.getInt("fidelidade"));
            }
        }
    }
    
    private void atualizar() throws SQLException {
        System.out.print("ID do plano a atualizar: ");
        int id = scanner.nextInt();
        scanner.nextLine();
        System.out.print("Novo nome: ");
        String nome = scanner.nextLine();
        System.out.print("Nova descrição: ");
        String descricao = scanner.nextLine();
        System.out.print("Novo valor: ");
        float valor = scanner.nextInt();
        System.out.print("Novo tempo de duração: ");
        int duracao = scanner.nextInt();
        System.out.print("Novo tempo de fidelidade: ");
        int fidelidade = scanner.nextInt();
        
        String sql = "UPDATE planos SET nome = ?, descricao = ?, valor = ?, duracao = ?, fidelidade = ? WHERE id_plano = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, nome);
            stmt.setString(2, descricao);
            stmt.setFloat(3, valor);
            stmt.setInt(4, duracao);
            stmt.setInt(5, fidelidade);
            stmt.setInt(6, id);
            int affectedRows = stmt.executeUpdate();

            if (affectedRows > 0) {
                System.out.println("Plano atualizado com sucesso!");
            } else {
                System.out.println("Plano não encontrado!");
            }
        }
    }
    
    private void deletar() throws SQLException {
        System.out.print("ID do plano a deletar: ");
        int id = scanner.nextInt();
        scanner.nextLine();
        
        String sql = "DELETE FROM planos WHERE id_plano = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            int affectedRows = stmt.executeUpdate();

            if (affectedRows > 0) {
                System.out.println("Plano deletado com sucesso!");
            } else {
                System.out.println("Plano não encontrado!");
            }
        }
    }

    private void vincular() throws SQLException {
        String sqlBeneficios = "SELECT * FROM beneficios";

        try (Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery(sqlBeneficios)) {
            System.out.println("Benefícios:");

            while (rs.next()) {
                System.out.printf("ID: %d | Nome: %s | Descrição: %s\n",
                    rs.getInt("id_beneficio"),
                    rs.getString("nome"),
                    rs.getString("descricao"));
            }
        }

        String sqlPlanos = "SELECT * FROM planos";

        try (Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery(sqlPlanos)) {
            System.out.println("Planos:");

            while (rs.next()) {
                System.out.printf("ID: %d | Nome: %s | Descrição: %s | Valor: %.2f\n",
                    rs.getInt("id_plano"),
                    rs.getString("nome"),
                    rs.getString("descricao"),
                    rs.getFloat("valor"));
            }
        }

        System.out.print("ID do benefício: ");
        int id_beneficio = scanner.nextInt();
        System.out.print("ID do plano: ");
        int id_plano = scanner.nextInt();
        
        String sql = "INSERT INTO planos_tem_beneficios (id_plano, id_beneficio) VALUES (?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id_plano);
            stmt.setInt(2, id_beneficio);
            stmt.executeUpdate();

            System.out.println("Vínculo cadastrado com sucesso!");
        }
    }
}