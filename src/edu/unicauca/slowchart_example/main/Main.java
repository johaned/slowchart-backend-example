package edu.unicauca.slowchart_example.main;

import java.net.UnknownHostException;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCursor;

import edu.unicauca.slowchart.logic.Parser;
import edu.unicauca.slowchart.logic.Processor;
import edu.unicauca.slowchart.persistence.model.Flow;
import edu.unicauca.slowchart_example.logic.Communicator;
import edu.unicauca.slowchart_example.logic.FlowFactory;
import edu.unicauca.slowchart_example.miscellaneus.Log;
import edu.unicauca.slowchart_example.persistence.conn.MongoDBAdapter;

public class Main {

	public static void main(String[] args) throws UnknownHostException {
		MongoDBAdapter mdbc = MongoDBAdapter.getInstance();
		DBCursor c;
		Flow f;
		// -- Erases the database
		mdbc.remove_all_doc_by_coll();
		// -- Creates test flow like a Mongo document
		BasicDBObject bdbo = new BasicDBObject();
		bdbo = FlowFactory.create_flow();
		// -- Saves test flow
		mdbc.insert_doc(bdbo);

		// -- Simulates interpreting flow process
		// - Inspects how many documents exist
		mdbc.see_all_doc_by_coll();
		// - Gets the flow objects (in this scenario only one exists)
		c = mdbc.get_cursor_by_coll();
		Communicator comm = new Communicator();
		Processor p = new Processor(comm);
		
		while (c.hasNext()) {
			f = Parser.to_convert_object((BasicDBObject) c.next());
			Log.print(f.getBranches().iterator().next().getRoute_for_true());
			Log.print(p.process(f));
		}

	}

}
