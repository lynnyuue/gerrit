// Copyright (C) 2013 The Android Open Source Project
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

package com.google.gerrit.server.account;

import com.google.gerrit.common.data.GlobalCapability;
import com.google.gerrit.extensions.annotations.RequiresCapability;
import com.google.gerrit.extensions.restapi.ResourceNotFoundException;
import com.google.gerrit.extensions.restapi.Response;
import com.google.gerrit.extensions.restapi.RestModifyView;
import com.google.gerrit.reviewdb.client.Account;
import com.google.gerrit.reviewdb.server.ReviewDb;
import com.google.gerrit.server.account.PutActive.Input;
import com.google.gwtorm.server.OrmException;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;
import java.io.IOException;
import java.util.concurrent.atomic.AtomicBoolean;

@RequiresCapability(GlobalCapability.MODIFY_ACCOUNT)
@Singleton
public class PutActive implements RestModifyView<AccountResource, Input> {
  public static class Input {}

  private final Provider<ReviewDb> dbProvider;
  private final AccountsUpdate.Server accountsUpdate;

  @Inject
  PutActive(Provider<ReviewDb> dbProvider, AccountsUpdate.Server accountsUpdate) {
    this.dbProvider = dbProvider;
    this.accountsUpdate = accountsUpdate;
  }

  @Override
  public Response<String> apply(AccountResource rsrc, Input input)
      throws ResourceNotFoundException, OrmException, IOException {
    AtomicBoolean alreadyActive = new AtomicBoolean(false);
    Account account =
        accountsUpdate
            .create()
            .atomicUpdate(
                dbProvider.get(),
                rsrc.getUser().getAccountId(),
                a -> {
                  if (a.isActive()) {
                    alreadyActive.set(true);
                  } else {
                    a.setActive(true);
                  }
                });
    if (account == null) {
      throw new ResourceNotFoundException("account not found");
    }
    return alreadyActive.get() ? Response.ok("") : Response.created("");
  }
}
