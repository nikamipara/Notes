package app.nikunj.notes;

import android.animation.Animator;
import android.content.Context;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewPropertyAnimator;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by nikam on 08-08-2015.
 */
public class NotesAdapter extends RecyclerView.Adapter<NotesAdapter.ViewHolder> {


    // Define the listener interface
    public interface OnItemClickListener {
        void onItemClick(View itemView, int position);
    }

    // Provide a direct reference to each of the views within a data item
// Used to cache the views within the item layout for fast access
    public class ViewHolder extends RecyclerView.ViewHolder {
        // Your holder should contain a member variable
        // for any view that will be set as you render a row
        public TextView title;
        public TextView body;
        public ImageView image;
        public View root;

        // We also create a constructor that accepts the entire item row
        // and does the view lookups to find each subview
        public ViewHolder(final View itemView) {
            super(itemView);
            this.root = itemView;
            this.title = (TextView) itemView.findViewById(R.id.tvName);
            this.body = (TextView) itemView.findViewById(R.id.tvHometown);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Triggers click upwards to the adapter on click
                    if (listener != null)
                        listener.onItemClick(itemView, getLayoutPosition());
                }
            });
        /*this.image = (ImageView)itemView.findViewById(R.id.imageView);*/
        }
    }  // Store a member variable for the users

    private ArrayList<Note> notes;
    // Store the context for later use
    private Context context;
    public OnItemClickListener listener;

    // Pass in the context and users array into the constructor
    public NotesAdapter(Context context, ArrayList<Note> notes) {
        this.notes = notes;
        this.context = context;
    }

    // Define the method that allows the parent activity or fragment to define the listener
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // Inflate the custom layout
        View itemView = LayoutInflater.from(context).
                inflate(R.layout.item_note, parent, false);
        itemView.setAlpha(1);
        itemView.setTranslationX(0);
        itemView.setTranslationY(0);
        ViewHolder holder = new ViewHolder(itemView);
        handleSwiping(itemView, holder);
        return holder;
    }

    // Involves populating data into the item through holder
    int variable = 0;

    @Override
    public void onBindViewHolder(NotesAdapter.ViewHolder holder, int position) {
        // Get the data model based on position

        // Set item views based on the data model
        //populate(holder,position);
        holder.root.setVisibility(View.VISIBLE);
        holder.root.setAlpha(1);
        holder.root.setTranslationX(0);
        holder.root.setTranslationY(0);
        populate(holder, position);
        setAnimation(holder.root, position);
        /*if (variable++ % 3 == 0)
            holder.image.setImageDrawable(context.getDrawable(R.drawable.one));
        else holder.image.setImageDrawable(context.getDrawable(R.drawable.two));*/
    }

    private void populate(ViewHolder holder, int position) {
        Note note = notes.get(position);
        int size = 30;/*
        int titlesize = size -note.title.length();
        if(titlesize<8)titlesize=;*/
        size = 60;
        int bodysize = size - note.body.length();
        if (bodysize < 10) bodysize = 10;
        holder.title.setText(note.title);
        holder.title.setTextSize(15);
        holder.body.setText(note.body);
        holder.body.setTextSize(bodysize);
    }

    // Return the total count of items
    @Override
    public int getItemCount() {
        return notes.size();
    }

    public void adddata(Note n) {
        notes.set(0, n);
        notifyItemChanged(0);
    }

    private int lastPosition = -1;

    private void setAnimation(View viewToAnimate, int position) {
        // If the bound view wasn't previously displayed on screen, it's animated
        if (position > lastPosition) {
            Animation animation = AnimationUtils.loadAnimation(context, android.R.anim.slide_in_left);
            viewToAnimate.startAnimation(animation);
            lastPosition = position;
        }
    }

    private void onSwipe(final View rootView, final int position, final boolean isToLeft) {
        ViewPropertyAnimator animator;
        if (isToLeft)
            animator = rootView.animate().translationX(-rootView.getWidth());
        else
            animator = rootView.animate().translationX(rootView.getWidth());
        animator.setListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                rootView.setVisibility(View.GONE);
                notes.remove(position);
                notifyItemRemoved(position);
            }

            @Override
            public void onAnimationCancel(Animator animation) {
            }

            @Override
            public void onAnimationRepeat(Animator animation) {
            }
        });
        animator.start();
    }

    private void handleSwiping(final View rootView, final RecyclerView.ViewHolder holder) {
        final GestureDetectorCompat gestureDetector = new GestureDetectorCompat(rootView.getContext(),
                new GestureDetector.OnGestureListener() {
                    @Override
                    public boolean onDown(MotionEvent e) {
                        return false;
                    }

                    @Override
                    public void onShowPress(MotionEvent e) {

                    }

                    @Override
                    public boolean onSingleTapUp(MotionEvent e) {
                        return false;
                    }

                    @Override
                    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
                        return false;
                    }

                    @Override
                    public void onLongPress(MotionEvent e) {

                    }

                    @Override
                    public boolean onFling(final MotionEvent e1, final MotionEvent e2, final float velocityX,
                                           final float velocityY) {
                        final int viewSwipeThreshold = rootView.getWidth() / 4;
                        if (velocityX < -viewSwipeThreshold) {
                            onSwipe(rootView, holder.getAdapterPosition(), false);
                            return true;
                        } else if (velocityX > viewSwipeThreshold) {
                            onSwipe(rootView, holder.getAdapterPosition(), true);
                            return true;
                        }
                        return false;
                    }
                });
        rootView.setOnTouchListener(new View.OnTouchListener() {
            private final float originalX = 0;
            private final float originalY = 0;
            private float startMoveX = 0;
            private float startMoveY = 0;

            @Override
            public boolean onTouch(final View view, final MotionEvent event) {
                final int viewSwipeHorizontalThreshold = rootView.getWidth() * 2 / 3;
                final int viewSwipeVerticalThreshold = rootView.getHeight() * 2 / 3;
                if (gestureDetector.onTouchEvent(event))
                    return true;
                final float x = event.getRawX(), y = event.getRawY();
                final float deltaX = x - startMoveX, deltaY = y - startMoveY;
                switch (event.getAction() & MotionEvent.ACTION_MASK) {
                    case MotionEvent.ACTION_DOWN:
                        startMoveX = x;
                        startMoveY = y;
                        break;
                    case MotionEvent.ACTION_UP:
                        if (Math.abs(deltaX) < viewSwipeHorizontalThreshold) {
                            rootView.animate().translationX(originalX).translationY(originalY).alpha(1).start();
                            if (Math.abs(deltaY) < viewSwipeHorizontalThreshold)
                                rootView.performClick();
                        } else if (deltaX < 0)
                            onSwipe(rootView, holder.getAdapterPosition(), true);
                        else
                            onSwipe(rootView, holder.getAdapterPosition(), false);
                        break;
                    case MotionEvent.ACTION_CANCEL:
                        if (Math.abs(deltaX) < viewSwipeHorizontalThreshold
                                || Math.abs(deltaY) < viewSwipeVerticalThreshold)
                            rootView.animate().translationX(originalX).translationY(originalY).alpha(1).start();
                        else if (deltaX < 0)
                            onSwipe(rootView, holder.getAdapterPosition(), true);
                        else

                            onSwipe(rootView, holder.getAdapterPosition(), false);
                        break;
                    case MotionEvent.ACTION_POINTER_DOWN:
                        break;
                    case MotionEvent.ACTION_POINTER_UP:
                        break;
                    case MotionEvent.ACTION_MOVE:
                        rootView.setAlpha(Math.max(Math.min((255 - Math.abs(deltaX)) / 255f, 1.0f), 0.1f));
                        rootView.setTranslationX(deltaX);
                        break;
                }
                return true;
            }
        });

    }
}
