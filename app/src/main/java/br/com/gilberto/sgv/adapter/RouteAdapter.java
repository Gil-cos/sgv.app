package br.com.gilberto.sgv.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import br.com.gilberto.sgv.R;
import br.com.gilberto.sgv.domain.address.Address;
import br.com.gilberto.sgv.domain.institution.Institution;
import br.com.gilberto.sgv.domain.route.Period;
import br.com.gilberto.sgv.domain.route.Route;

public class RouteAdapter extends RecyclerView.Adapter<RouteAdapter.MyViewHolder> {

    List<Route> routes;
    Context context;

    public RouteAdapter(List<Route> routes, Context context) {
        this.routes = routes;
        this.context = context;
    }

    @NonNull
    @Override
    public RouteAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View item = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_route, parent, false);
        return new MyViewHolder(item);
    }

    @Override
    public void onBindViewHolder(@NonNull RouteAdapter.MyViewHolder holder, int position) {

        Route route = routes.get(position);
        Institution institution = route.getInstitution();
        Address address = institution.getAddress();

        holder.description.setText(route.getDescription());
        holder.institution.setText(institution.getName());
        holder.period.setText(route.getPeriod().getPrettyName());
        holder.street.setText(address.getStreet());
        holder.cep.setText(address.getCep());
        holder.neighborhood.setText(address.getNeighborhood());

        if (route.getPeriod().equals(Period.DAYTIME)) {
            holder.periodImage.setImageResource(R.drawable.ic_baseline_wb_sunny_24);
        }

    }

    @Override
    public int getItemCount() {
        return routes.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView description, period, institution, street, cep, neighborhood;
        ImageView periodImage;

        public MyViewHolder(View itemView) {
            super(itemView);

            description = itemView.findViewById(R.id.routeAdapterDescription);
            institution = itemView.findViewById(R.id.routeAdapterInstitution);
            period = itemView.findViewById(R.id.routeAdapterPeriod);
            street = itemView.findViewById(R.id.routeAdapterAddressStreet);
            cep = itemView.findViewById(R.id.routeAdapterAddressCep);
            neighborhood = itemView.findViewById(R.id.routeAdapterAddressNeighborhood);
            periodImage = itemView.findViewById(R.id.imageViewPeriod);

        }

    }
}
