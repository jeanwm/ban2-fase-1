// Arquivo: AcademiaApp.java
import java.util.Scanner;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class AcademiaApp {    
    public static void main(String[] args) {
        try {
            Conexao c = new Conexao();
            Connection connection = c.getConnection();

            Scanner scanner = new Scanner(System.in);
            while (true) {
                System.out.println("\n=== SISTEMA ACADEMIA ===");
                System.out.println("1. Benefícios");
                System.out.println("2. Planos");
                System.out.println("3. Clientes");
                System.out.println("4. Funcionários");
                System.out.println("5. Telefones");
                System.out.println("6. Cargos");
                System.out.println("7. Manutenções");
                System.out.println("8. Equipamentos");
                System.out.println("9. Relatórios");
                System.out.println("0. Sair");
                System.out.print("Escolha uma opção: ");
                
                int opcao = scanner.nextInt();
                scanner.nextLine();
                
                switch (opcao) {
                    case 1 -> new BeneficioCRUD(connection, scanner).menu();
                    case 2 -> new PlanoCRUD(connection, scanner).menu();
                    case 3 -> new ClienteCRUD(connection, scanner).menu();
                    case 4 -> new FuncionarioCRUD(connection, scanner).menu();
                    case 5 -> new TelefoneCRUD(connection, scanner).menu();
                    case 6 -> new CargoCRUD(connection, scanner).menu();
                    case 7 -> new ManutencaoCRUD(connection, scanner).menu();
                    case 8 -> new EquipamentoCRUD(connection, scanner).menu();
                    case 9 -> new Relatorios(connection, scanner).menu();
                    case 0 -> { return; }
                    default -> System.out.println("Opção inválida!");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}