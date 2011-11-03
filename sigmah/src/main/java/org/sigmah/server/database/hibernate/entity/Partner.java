/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.server.database.hibernate.entity;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

/**
 * 
 * @author Alex Bertram
 * 
 */
@Entity
@Table(name = "Partner")
public class Partner implements java.io.Serializable, SchemaElement {

    private static final long serialVersionUID = -5985734789552797994L;

    private int id;
    private String name;
    private String fullName;
    private Set<UserDatabase> databases = new HashSet<UserDatabase>(0);

    public Partner() {
    }


    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "PartnerId", unique = true, nullable = false)
    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Column(name = "Name", nullable = false, length = 16)
    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Column(name = "FullName", length = 64)
    public String getFullName() {
        return this.fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    @Override
    public String toString() {
        return getName();
    }

    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinTable(name = "PartnerInDatabase", joinColumns = { @JoinColumn(name = "PartnerId", nullable = false, updatable = false) }, inverseJoinColumns = { @JoinColumn(name = "DatabaseId", nullable = false, updatable = false) })
    public Set<UserDatabase> getDatabases() {
        return this.databases;
    }

    public void setDatabases(Set<UserDatabase> databases) {
        this.databases = databases;
    }

    @Override
    public boolean equals(Object obj) {

        if (obj == null) {
            return false;
        }

        if (!(obj instanceof Partner)) {
            return false;
        }

        final Partner other = (Partner) obj;

        return id == other.id;
    }

    @Override
    public int hashCode() {
        return id;
    }
}