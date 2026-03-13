package br.inadThebest;

import br.inadThebest.model.DadosInadimplencia;
import br.inadThebest.thebest.HtmlInadTheBest;
import br.inadThebest.model.WebhookService;
import br.com.sankhya.jape.EntityFacade;
import br.com.sankhya.jape.core.JapeSession;
import br.com.sankhya.jape.dao.JdbcWrapper;
import br.com.sankhya.jape.wrapper.JapeFactory;
import br.com.sankhya.jape.wrapper.JapeWrapper;
import br.com.sankhya.modelcore.util.EntityFacadeFactory;
import org.cuckoo.core.ScheduledAction;
import org.cuckoo.core.ScheduledActionContext;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class AcAgNotiInadTheBest implements ScheduledAction {

    @Override
    public void onTime(ScheduledActionContext ctx) {
        List<DadosInadimplencia> inadimplencias = new ArrayList<>();

        // 🔒 Evita execução simultânea ou duplicada
        if (Boolean.TRUE.equals(JapeSession.getProperty("inadimplencia_thebest_rodando"))) {
            WebhookService.post("⚠️ Execução ignorada: processo de inadimplência da The Best já em andamento.");
            return;
        }
        JapeSession.putProperty("inadimplencia_thebest_rodando", true);

        try {
            EntityFacade facade = EntityFacadeFactory.getDWFFacade();
            JdbcWrapper jdbc = facade.getJdbcWrapper();
            jdbc.openSession();

            String sql = "SELECT " +
                    "    MAT.nomeparc AS Matriz, " +
                    "    PAR.nomeparc AS NomeParceiro, " +
                    "    FIN.NUFIN AS CodigoTitulo, " +
                    "    FIN.dtvenc AS DataVencimento, " +
                    "    FIN.numnota AS NumeroNota, " +
                    "    TRUNC(SYSDATE) - TRUNC(FIN.dtvenc) AS DiasVencido, " +
                    "    FIN.VLRDESDOB AS Valor " +
                    "FROM TGFFIN FIN " +
                    "INNER JOIN TGFPAR PAR ON PAR.codparc = FIN.codparc " +
                    "INNER JOIN TGFPAR MAT ON PAR.codparcmatriz = MAT.codparc " +
                    "WHERE FIN.RECDESP = 1 " +
                    "AND FIN.PROVISAO = 'N' " +
                    "AND PAR.CLIENTE = 'S' " +
                    "AND FIN.DTVENC < TRUNC(CURRENT_DATE) " +
                    "AND FIN.DHBAIXA IS NULL " +
                    "AND MAT.codparc = 3930";

            try (PreparedStatement ps = jdbc.getPreparedStatement(sql);
                 ResultSet rs = ps.executeQuery()) {

                while (rs.next()) {
                    DadosInadimplencia dado = new DadosInadimplencia();
                    dado.setMatriz(rs.getString("MATRIZ"));
                    dado.setNomeParceiro(rs.getString("NOMEPARCEIRO"));
                    dado.setCodigoTitulo(rs.getInt("CODIGOTITULO"));

                    Timestamp ts = rs.getTimestamp("DATAVENCIMENTO");
                    dado.setDataVencimento(ts != null ? ts.toLocalDateTime().toLocalDate() : null);

                    dado.setNumeroNota(rs.getInt("NUMERONOTA"));
                    dado.setDiasVencido(rs.getInt("DIASVENCIDO"));
                    dado.setValor(rs.getBigDecimal("VALOR"));

                    inadimplencias.add(dado);
                }
            }

            if (!inadimplencias.isEmpty()) {
                enviarEmails(inadimplencias);
                WebhookService.post("Inadimplência The Best processada com sucesso. Total: " + inadimplencias.size());
            } else {
                WebhookService.post("Nenhuma inadimplência da The Best encontrada para envio.");
            }

        } catch (Exception e) {
            WebhookService.post("Erro ao processar inadimplência da The Best: " + e.getMessage());
        } finally {
            // 🔓 Libera a trava para próximas execuções
            JapeSession.putProperty("inadimplencia_thebest_rodando", false);
            JapeSession.close();
        }
    }

    private void enviarEmails(List<DadosInadimplencia> inadimplencias) throws Exception {
        String emailDestino = "gisele@profood.com.br";
        String emailsEmCopia = "deyse@tuicial.com.br,comercial@profood.com.br,cobranca2@tuicial.com.br,matheus@profood.com.br";

        String html = HtmlInadTheBest.gerarHtmlEmail(inadimplencias);

        String emailFinal = emailDestino + "," + emailsEmCopia;

        JapeWrapper emailDAO = JapeFactory.dao("MSDFilaMensagem");

        emailDAO.create()
                .set("STATUS", "Pendente")
                .set("CODCON", BigDecimal.ZERO)
                .set("TENTENVIO", BigDecimal.ONE)
                .set("MENSAGEM", html.toCharArray())
                .set("TIPOENVIO", "E")
                .set("MAXTENTENVIO", BigDecimal.valueOf(3))
                .set("ASSUNTO", "Relatório de Inadimplência | The Best | " +
                        YearMonth.now().format(DateTimeFormatter.ofPattern("MMM/yyyy", new Locale("pt", "BR"))))
                .set("EMAIL", emailFinal)
                .set("CODUSU", BigDecimal.ZERO)
                .set("REENVIAR", "N")
                .save();
    }
}
