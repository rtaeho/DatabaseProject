//import javax.swing.table.AbstractTableModel;
//import java.util.List;
//
//public class BookingTableModel extends AbstractTableModel {
//	private final List<Booking> bookings;
//	private final String[] columnNames = { "BookingID", "Payment", "PaymentStatus", "Amount", "CustomerID",
//			"PaymentDate" };
//
//	public BookingTableModel(List<Booking> bookings) {
//		this.bookings = bookings;
//	}
//
//	@Override
//	public int getRowCount() {
//		return bookings.size();
//	}
//
//	@Override
//	public int getColumnCount() {
//		return columnNames.length;
//	}
//
////	@Override
////	public Object getValueAt(int rowIndex, int columnIndex) {
////		Booking booking = bookings.get(rowIndex);
////		switch (columnIndex) {
////		case 0:
////			return booking.getBookingID();
////		case 1:
////			return booking.getPayment();
////		case 2:
////			return booking.getPaymentStatus();
////		case 3:
////			return booking.getAmount();
////		case 4:
////			return booking.getCustomerID();
////		case 5:
////			return booking.getPaymentDate();
////		default:
////			return null;
////		}
////	}
//
//	@Override
//	public String getColumnName(int column) {
//		return columnNames[column];
//	}
//
//	@Override
//	public Object getValueAt(int rowIndex, int columnIndex) {
//		// TODO Auto-generated method stub
//		return null;
//	}
//}
