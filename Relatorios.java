// Arquivo: Relatorios.java
import java.sql.*;
import java.util.Scanner;

public class Relatorios {
    private Connection connection;
    private Scanner scanner;
    
    public Relatorios(Connection connection, Scanner scanner) {
        this.connection = connection;
        this.scanner = scanner;
    }
    
    public void menu() throws SQLException {
        while (true) {
            System.out.println("\n=== RELATÓRIOS ===");
            System.out.println("1. Clientes por plano");
            System.out.println("2. Manutenções pendentes");
            System.out.println("3. Funcionários ativos por cargo");
            System.out.println("4. Voltar");
            System.out.print("Escolha: ");
            
            int opcao = scanner.nextInt();
            scanner.nextLine();
            
            switch (opcao) {
                case 1 -> clientesPorPlano();
                case 2 -> manutencoesPendentes();
                case 3 -> funcionariosAtivosPorCargo();
                case 4 -> { return; }
                default -> System.out.println("Opção inválida!");
            }
        }
    }
    
    private void clientesPorPlano() throws SQLException {
        String sql = """
            SELECT p.nome as plano, COUNT(c.id_cliente) as quantidade
            FROM planos p
            LEFT JOIN clientes c ON p.id_plano = c.id_plano
            GROUP BY p.id_plano, p.nome
            """;
            
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            System.out.println("\n--- CLIENTES POR PLANO ---");
            while (rs.next()) {
                System.out.printf("Plano: %s | Clientes: %d\n",
                    rs.getString("plano"),
                    rs.getInt("quantidade"));
            }
        }
    }
    
    private void manutencoesPendentes() throws SQLException {
        String sql = """
            SELECT e.modelo, m.data_prevista, m.custo_previsto
            FROM manutencoes m
            JOIN equipamentos e ON m.id_equipamento = e.id_equipamento
            WHERE m.status = 0
            """;
            
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            System.out.println("\n--- MANUTENÇÕES PENDENTES ---");
            while (rs.next()) {
                System.out.printf("Equipamento: %s | Data Prevista: %s | Custo: R$%.2f\n",
                    rs.getString("modelo"),
                    rs.getDate("data_prevista"),
                    rs.getDouble("custo_previsto"));
            }
        }
    }
    
    private void funcionariosAtivosPorCargo() throws SQLException {
        String sql = """
            SELECT c.nome as cargo, COUNT(f.id_funcionario) as quantidade
            FROM cargos c
            LEFT JOIN funcionarios f ON c.id_cargo = f.id_cargo AND f.status = 1
            GROUP BY c.id_cargo, c.nome
            """;
            
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            System.out.println("\n--- FUNCIONÁRIOS ATIVOS POR CARGO ---");
            while (rs.next()) {
                System.out.printf("Cargo: %s | Funcionários Ativos: %d\n",
                    rs.getString("cargo"),
                    rs.getInt("quantidade"));
            }
        }
    }
}