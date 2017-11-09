final class geo {
	public int id;
	public String location;
	public float lat;
	public float lon;
	
	public geo(int id){
		this.id = id;
		this.location = new String();
		this.lat = 0;
		this.lon = 0;
	}
	
	public geo(int id, String location, float lat, float lon){
		this.id = id;
		this.location = location;
		this.lat = lat;
		this.lon = lon;
	}
	
	public int getID() {
		return id;
	}
	
	public String getLocation() {
		return location;
	}
	
	public float getLon() {
		return lon;
	}
	
	public float getLat() {
		return lat;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + id;
		result = prime * result + Float.floatToIntBits(lat);
		result = prime * result + ((location == null) ? 0 : location.hashCode());
		result = prime * result + Float.floatToIntBits(lon);
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		geo other = (geo) obj;
		if (id != other.id)
			return false;
		if (Float.floatToIntBits(lat) != Float.floatToIntBits(other.lat))
			return false;
		if (location == null) {
			if (other.location != null)
				return false;
		} else if (!location.equals(other.location))
			return false;
		if (Float.floatToIntBits(lon) != Float.floatToIntBits(other.lon))
			return false;
		return true;
	}
	
}
