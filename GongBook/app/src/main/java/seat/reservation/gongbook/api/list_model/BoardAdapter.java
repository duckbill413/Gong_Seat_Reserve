package seat.reservation.gongbook.api.list_model;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import seat.reservation.gongbook.R;

public class BoardAdapter extends ArrayAdapter {

    private List<Board> boards;
    private LayoutInflater layoutInflater;
    private Context context;

    public BoardAdapter(Context context, ArrayList list) {
        super(context, 0, list);
        this.boards = list;
        this.context = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            layoutInflater = LayoutInflater.from(parent.getContext());
            convertView = layoutInflater.inflate(R.layout.item_layout, parent, false);
        }
        // ViewHolder 에 필요한 데이터들을 적음.
        TextView title = convertView.findViewById(R.id.item_board_title);
        TextView contents = convertView.findViewById(R.id.item_board_content);
        TextView time = convertView.findViewById(R.id.item_board_date);

        Board data = boards.get(position);
        title.setText(data.getTitle());
        contents.setText(data.getContents());
        time.setText(data.getTime());
        return convertView;
    }
}
