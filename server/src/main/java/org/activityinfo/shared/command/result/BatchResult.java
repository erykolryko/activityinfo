package org.activityinfo.shared.command.result;

/*
 * #%L
 * ActivityInfo Server
 * %%
 * Copyright (C) 2009 - 2013 UNICEF
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the 
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public 
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/gpl-3.0.html>.
 * #L%
 */

import java.util.List;

/**
 * 
 * @author Alex Bertram
 */
public class BatchResult implements CommandResult {

    private List<CommandResult> results;

    public BatchResult() {
    }

    public BatchResult(List<CommandResult> results) {
        this.results = results;
    }

    public List<CommandResult> getResults() {
        return results;
    }

    public void setResults(List<CommandResult> results) {
        this.results = results;
    }

    public CommandResult getLastResult() {
        return results.get(results.size() - 1);
    }

    public <X extends CommandResult> X getResult(int index) {
        return (X) results.get(index);
    }
}
