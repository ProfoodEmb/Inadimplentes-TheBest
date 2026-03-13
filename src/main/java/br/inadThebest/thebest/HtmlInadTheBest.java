package br.inadThebest.thebest;

import br.inadThebest.model.DadosInadimplencia;
import java.util.Objects;
import java.time.YearMonth;
import java.util.Map;
import java.util.stream.Collectors;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;

public class HtmlInadTheBest {

    private static String truncate(String text, int maxLength) {
        if (text == null) return "";
        return text.length() <= maxLength ? text : text.substring(0, maxLength - 3) + "...";
    }

    public static String gerarHtmlEmail(List<DadosInadimplencia> dados) {
        if (dados == null || dados.isEmpty()) {
            return "<html><body><p>Nenhuma inadimplência encontrada.</p></body></html>";
        }

        //Ordenação de Dias Vencidos
        dados.sort((d1, d2) -> Integer.compare(d2.getDiasVencido(), d1.getDiasVencido()));

        StringBuilder sb = new StringBuilder();
        NumberFormat nf = NumberFormat.getCurrencyInstance(new Locale("pt", "BR"));
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy");


        sb.append("<!DOCTYPE html><html lang=\"pt-BR\"><head><meta charset=\"UTF-8\">");
        sb.append("<meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">");
        sb.append("<title>Relatório de Inadimplência</title></head>");
        sb.append("<body style=\"background-color:#f5f5f5;font-family:'Inter', Arial, sans-serif; margin:0; padding:0; color:#333;\">");

        sb.append("<table width=\"100%\" cellpadding=\"0\" cellspacing=\"0\" style=\"padding:20px; font-family:Arial, sans-serif; color:#333;\">");
        sb.append("<tr><td align=\"center\">");

        sb.append("<table width=\"800\" style=\"background-color:#fff; border-radius:12px; box-shadow:0 2px 6px rgba(0,0,0,0.1);\">");

        sb.append("<tr><td align=\"center\" style=\"padding:15px 0;\">");
        sb.append("<img src=\"https://imgur.com/uyCBGU6.png\" alt=\"Logo\" style=\"max-width:200px;\"></td></tr>");

        sb.append("<tr><td style=\"padding:0 20px 10px 20px; text-align:center;\">");
        String mesAno = YearMonth.now().format(DateTimeFormatter.ofPattern("MMM/yyyy", new Locale("pt", "BR")));
        sb.append("<h2 style=\"color:#333; margin-bottom:8px;\">Relatório de Inadimplência - The Best - ").append(mesAno).append("</h2>");
        sb.append("</td></tr>");

        BigDecimal valorTotalGeral = dados.stream()
                .map(DadosInadimplencia::getValor)
                .filter(v -> v != null)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        long quantidadeParceirosUnicos = dados.stream()
                .map(DadosInadimplencia::getNomeParceiro)
                .filter(Objects::nonNull)
                .map(s -> s.trim().toUpperCase())
                .distinct()
                .count();

        long quantidadeTitulos = dados.size();

        //Card de Resumo
        sb.append("<tr><td align=\"center\">");
        sb.append("<div style=\"margin-bottom:20px; padding:10px; background-color:#A62B1F; color:#fff; border-radius:8px; width:760px; font-size:12px\">");
        sb.append("<strong>Valor Total de Inadimplência:</strong> ").append(nf.format(valorTotalGeral));
        sb.append(" &nbsp; | &nbsp; ");
        sb.append("<strong>Quantidade Total de Inadimplentes:</strong> ").append(quantidadeParceirosUnicos);
        sb.append(" &nbsp; | &nbsp; ");
        sb.append("<strong>Quantidade de Títulos:</strong> ").append(quantidadeTitulos);
        sb.append("</div>");
        sb.append("</td></tr>");

        sb.append("<tr><td style=\"padding:0 20px 20px 20px;\">");
        sb.append("<table width=\"100%\" cellpadding=\"6\" cellspacing=\"0\" style=\"border-collapse:collapse; font-size:14px; color:#333; min-width:1000px;\">");
        sb.append("<thead style=\"background-color:#A62B1F; color:#fff; text-align:left;\">");
        sb.append("<tr>");
        sb.append("<th style=\"padding:8px; min-width:150px;\">Matriz</th>");
        sb.append("<th style=\"padding:8px; min-width:150px;\">Nome Parceiro</th>");
        sb.append("<th style=\"padding:8px; white-space:nowrap;\">Código Título</th>");
        sb.append("<th style=\"padding:8px; white-space:nowrap;\">Data Vencimento</th>");
        sb.append("<th style=\"padding:8px; white-space:nowrap;\">Número Nota</th>");
        sb.append("<th style=\"padding:8px; white-space:nowrap;\">Dias Vencido</th>");
        sb.append("<th style=\"padding:8px; text-align:right;\">Valor</th>");
        sb.append("</tr></thead><tbody>");

        Map<String, List<DadosInadimplencia>> porMatriz = dados.stream()
                .collect(Collectors.groupingBy(DadosInadimplencia::getMatriz));

        for (Map.Entry<String, List<DadosInadimplencia>> entry : porMatriz.entrySet()) {
            String matriz = entry.getKey();
            List<DadosInadimplencia> lista = entry.getValue();

            BigDecimal totalMatriz = BigDecimal.ZERO;
            boolean zebra = false;

            //Cabeçalho
            sb.append("<tr style=\"background-color:#ddd;\"><td colspan=\"7\" style=\"padding:8px; font-weight:bold;\">")
                    .append(matriz).append("</td></tr>");

            //Ordenação
            lista.sort((d1, d2) -> Integer.compare(d2.getDiasVencido(), d1.getDiasVencido()));

            for (DadosInadimplencia d : lista) {
                String bgColor = zebra ? "#f9f9f9" : "#ffffff";
                zebra = !zebra;

                sb.append("<tr style=\"background-color:").append(bgColor).append("; border-bottom:1px solid #eee;\">");
                sb.append("<td style=\"padding:8px; white-space:nowrap;\">").append(truncate(d.getMatriz(), 30)).append("</td>");
                sb.append("<td style=\"padding:8px; white-space:nowrap;\">").append(truncate(d.getNomeParceiro(), 30)).append("</td>");
                sb.append("<td style=\"padding:8px; white-space:nowrap;\">").append(d.getCodigoTitulo()).append("</td>");
                sb.append("<td style=\"padding:8px; white-space:nowrap;\">")
                        .append(d.getDataVencimento() != null ? d.getDataVencimento().format(dtf) : "").append("</td>");
                sb.append("<td style=\"padding:8px; white-space:nowrap;\">").append(d.getNumeroNota()).append("</td>");
                sb.append("<td style=\"padding:8px; white-space:nowrap;\">").append(d.getDiasVencido()).append("</td>");
                sb.append("<td style=\"padding:8px; text-align:right; white-space:nowrap;\"><strong>")
                        .append("&nbsp;").append(nf.format(d.getValor())).append("</strong></td>");
                sb.append("</tr>");

                if (d.getValor() != null) totalMatriz = totalMatriz.add(d.getValor());
            }

            sb.append("<tr style=\"background-color:#A62B1F; color:#fff; font-weight:bold;\">");
            sb.append("<td colspan=\"6\" style=\"padding:8px; text-align:right;\">Total Matriz:</td>");
            sb.append("<td style=\"padding:8px; text-align:right; white-space:nowrap;\">&nbsp;").append(nf.format(totalMatriz)).append("</td>");
            sb.append("</tr>");

            sb.append("<tr><td colspan=\"7\" style=\"height:25px;\"></td></tr>");
        }

        sb.append("</tbody></table></td></tr>");

        sb.append("<tr><td style=\"padding:0 20px 20px 20px;\">");
        sb.append("<div style=\"margin-top:20px; height:16px; background-color:#A62B1F; border-radius:0 0 12px 12px;\"></div>");
        sb.append("</td></tr>");

        sb.append("</table></td></tr></table></body></html>");

        return sb.toString();
    }

}
