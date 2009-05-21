// Copyright (C) 2009 The Android Open Source Project
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
// http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package com.google.gerrit.server.mail;

import com.google.gerrit.client.reviewdb.Change;
import com.google.gerrit.server.GerritServer;

import javax.mail.MessagingException;

/** Send notice about a change failing to merged. */
public class MergeFailSender extends ReplyToChangeSender {
  public MergeFailSender(GerritServer gs, Change c) {
    super(gs, c, "comment");
  }

  @Override
  protected void init() throws MessagingException {
    super.init();

    ccExistingReviewers();
  }

  @Override
  protected void format() {
    appendText("Change " + change.getChangeId());
    if (patchSetInfo != null && patchSetInfo.getAuthor() != null
        && patchSetInfo.getAuthor().getName() != null) {
      appendText(" by ");
      appendText(patchSetInfo.getAuthor().getName());
    }
    appendText(" FAILED to submit to ");
    appendText(change.getDest().getShortName());
    appendText(".\n\n");
    formatCoverLetter();
  }
}
