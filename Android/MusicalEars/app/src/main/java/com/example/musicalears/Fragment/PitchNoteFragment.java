package com.example.musicalears.Fragment;

import android.os.Bundle;
import android.os.CountDownTimer;

import com.example.musicalears.PitchMatchingActivity;
import com.example.musicalears.R;

import java.util.Objects;

public class PitchNoteFragment extends NoteFragment {
    private static final String PARAM_DURATION = "duration";
    private static final String PARAM_SHOULD_SHOW_NOTE_NAME = "showName";

    private long paramDuration;
    private boolean paramShouldShowNoteName;

    public PitchNoteFragment() {
        // Required empty public constructor
    }

    public static PitchNoteFragment newInstance(long duration, boolean showNote) {
        PitchNoteFragment fragment = new PitchNoteFragment();
        Bundle args = new Bundle();
        args.putLong(PARAM_DURATION, duration);
        args.putBoolean(PARAM_SHOULD_SHOW_NOTE_NAME, showNote);
        fragment.setArguments(args);
        return fragment;
    }

    public void processPitch(float pitchInHz) {
        float[] noteBubbleParams = super.getNoteBubbleParams(pitchInHz);
        super.moveBubble(noteBubbleParams);
        if (checkAccuracy(noteBubbleParams[0])) {
            noteBubble.setImageResource(R.drawable.note_bubble_on);
            updateTimer();
        } else {
            noteBubble.setImageResource(R.drawable.note_bubble_off);
            stopTimer();
        }
    }

    @Override
    public void setTargetNote(int adjustedIndex) {
        super.setTargetNote(adjustedIndex);
        if (paramShouldShowNoteName && targetNote != null)
            targetNoteText.setText(targetNote.getNoteName());
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() == null) {
            return;
        }
        paramDuration = getArguments().getLong(PARAM_DURATION);
        paramShouldShowNoteName = getArguments().getBoolean(PARAM_SHOULD_SHOW_NOTE_NAME);
    }

    private void updateTimer() {
        if (accuracyTimer != null) {
            return;
        }
        accuracyTimer = new CountDownTimer(paramDuration, 1000) {
            @Override
            public void onTick(long l) {
            }

            @Override
            public void onFinish() {
                ((PitchMatchingActivity) Objects.requireNonNull(getActivity())).scorePointAndReset();
            }
        }.start();
        super.updateTimer(paramDuration);
    }
}