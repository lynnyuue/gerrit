// Copyright (C) 2011 The Android Open Source Project
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

package com.google.gerrit.reviewdb;

import com.google.gwtorm.client.Column;
import com.google.gwtorm.client.CompoundKey;

/** Membership of an {@link AccountGroup} in an {@link AccountGroup}. */
public final class AccountGroupInclude {
  public static class Key extends CompoundKey<AccountGroup.Id> {
    private static final long serialVersionUID = 1L;

    @Column(id = 1)
    protected AccountGroup.Id groupId;

    @Column(id = 2)
    protected AccountGroup.Id includeId;

    protected Key() {
      groupId = new AccountGroup.Id();
      includeId = new AccountGroup.Id();
    }

    public Key(final AccountGroup.Id g, final AccountGroup.Id i) {
      groupId = g;
      includeId = i;
    }

    @Override
    public AccountGroup.Id getParentKey() {
      return groupId;
    }

    public AccountGroup.Id getGroupId() {
      return groupId;
    }

    public AccountGroup.Id getIncludeId() {
      return includeId;
    }

    @Override
    public com.google.gwtorm.client.Key<?>[] members() {
      return new com.google.gwtorm.client.Key<?>[] {includeId};
    }
  }

  @Column(id = 1, name = Column.NONE)
  protected Key key;

  protected AccountGroupInclude() {
  }

  public AccountGroupInclude(final AccountGroupInclude.Key k) {
    key = k;
  }

  public AccountGroupInclude.Key getKey() {
    return key;
  }

  public AccountGroup.Id getGroupId() {
    return key.groupId;
  }

  public AccountGroup.Id getIncludeId() {
    return key.includeId;
  }
}
