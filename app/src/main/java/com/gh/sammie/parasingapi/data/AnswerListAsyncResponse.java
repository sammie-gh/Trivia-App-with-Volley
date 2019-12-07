package com.gh.sammie.parasingapi.data;


import com.gh.sammie.parasingapi.modal.Question;

import java.util.ArrayList;

public interface AnswerListAsyncResponse {
    void processFinished(ArrayList<Question> questionArrayList);
}
