import javax.swing.table.AbstractTableModel;
import java.util.List;

public class MovieTableModel extends AbstractTableModel {
    private final List<Movie> movies;
    private final String[] columnNames = {"MovieID", "Title", "MovieTime", "Rating", "Director", "Genre", "Introduction", "ReleaseDate", "Score"};

    public MovieTableModel(List<Movie> movies) {
        this.movies = movies;
    }

    @Override
    public int getRowCount() {
        return movies.size();
    }

    @Override
    public int getColumnCount() {
        return columnNames.length;
    }

//    @Override
//    public Object getValueAt(int rowIndex, int columnIndex) {
//        Movie movie = movies.get(rowIndex);
//        switch (columnIndex) {
//            case 0: return movie.getMovieID();
//            case 1: return movie.getTitle();
//            case 2: return movie.getMovieTime();
//            case 3: return movie.getRating();
//            case 4: return movie.getDirector();
//            case 5: return movie.getGenre();
//            case 6: return movie.getIntroduction();
//            case 7: return movie.getReleaseDate();
//            case 8: return movie.getScore();
//            default: return null;
//        }
//    }

    @Override
    public String getColumnName(int column) {
        return columnNames[column];
    }

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		// TODO Auto-generated method stub
		return null;
	}
}
