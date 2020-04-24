import com.hp.gagawa.java.elements.*;

import java.io.*;
import java.util.List;

public class HTMLBuilder {
    private PrintWriter out;

    private Html html;
    private Body body;

    private Table table;

    HTMLBuilder() {
        try {
            File file = new File("./src/main/resources/index.html");
            out = new PrintWriter(new FileOutputStream(file));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void build() {
        html = new Html();
        Head head = new Head();

        Title title = new Title().appendChild(new Text("Results"));
        head.appendChild(title);

        Link link = new Link().setRel("stylesheet").setHref("style.css");
        head.appendChild(link);

        html.appendChild(head);

        body = new Body();
    }

    public void createTable() {
        table = buildTable();
    }

    public void finishTable() {
        body.appendChild(table);
    }

    public void addResult(int matrixSize, double time1, List<Pair<Double, Double>> results) {
        Tr tr = new Tr();
        tr.appendChild(new Td().appendText(Integer.toString(matrixSize)));
        tr.appendChild(new Td().appendText(Double.toString(time1)));

        for (Pair<Double, Double> value : results) {
            tr.appendChild(new Td().appendText(Double.toString(value.getX())));
            tr.appendChild(new Td().appendText(Double.toString(value.getY())));
        }

        table.appendChild(tr);
    }

    public void finish() {
        html.appendChild(body);
        out.println(html.write());
        out.close();
    }

    private Table buildTable() {
        table = new Table();
        addHeading();
        return table;
    }

    private void addHeading() {
        Tr heading = new Tr();
        heading.appendChild(new Th().appendText("Matrix size").setRowspan("3"));
        heading.appendChild(new Th().appendText("Sequential algorithm").setRowspan("3"));
        heading.appendChild(new Th().appendText("Parallel algorithm").setColspan("4"));

        table.appendChild(heading);

        Tr processorsNumber = new Tr();
        processorsNumber.appendChild(new Th().appendText("2 processes").setColspan("2"));
        processorsNumber.appendChild(new Th().appendText("4 processes").setColspan("2"));

        table.appendChild(processorsNumber);

        Tr parameters = new Tr();

        for (int i = 0; i < 2; i++) {
            parameters.appendChild(new Th().appendText("Time"));
            parameters.appendChild(new Th().appendText("Acceleration"));
        }

        table.appendChild(parameters);
    }
}
