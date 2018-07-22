import React from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, Label } from 'reactstrap';
import { AvForm, AvGroup, AvInput, AvField } from 'availity-reactstrap-validation';
// tslint:disable-next-line:no-unused-variable
import { Translate, translate, ICrudGetAction, ICrudGetAllAction, ICrudPutAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { IRootState } from 'app/shared/reducers';

import { IProduct } from 'app/shared/model/product.model';
import { getEntities as getProducts } from 'app/entities/product/product.reducer';
import { IEvent } from 'app/shared/model/event.model';
import { getEntities as getEvents } from 'app/entities/event/event.reducer';
import { getEntity, updateEntity, createEntity, reset } from './sales.reducer';
import { ISales } from 'app/shared/model/sales.model';
// tslint:disable-next-line:no-unused-variable
import { convertDateTimeFromServer } from 'app/shared/util/date-utils';
import { keysToValues } from 'app/shared/util/entity-utils';

export interface ISalesUpdateProps extends StateProps, DispatchProps, RouteComponentProps<{ id: number }> {}

export interface ISalesUpdateState {
  isNew: boolean;
  idsevent: any[];
  productId: number;
}

export class SalesUpdate extends React.Component<ISalesUpdateProps, ISalesUpdateState> {
  constructor(props) {
    super(props);
    this.state = {
      idsevent: [],
      productId: 0,
      isNew: !this.props.match.params || !this.props.match.params.id
    };
  }

  componentDidMount() {
    if (this.state.isNew) {
      this.props.reset();
    } else {
      this.props.getEntity(this.props.match.params.id);
    }

    this.props.getProducts();
    this.props.getEvents();
  }

  saveEntity = (event, errors, values) => {
    if (errors.length === 0) {
      const { salesEntity } = this.props;
      const entity = {
        ...salesEntity,
        ...values
      };

      if (this.state.isNew) {
        this.props.createEntity(entity);
      } else {
        this.props.updateEntity(entity);
      }
      this.handleClose();
    }
  };

  handleClose = () => {
    this.props.history.push('/entity/sales');
  };

  productUpdate = element => {
    const id = element.target.value.toString();
    if (id === '') {
      this.setState({
        productId: -1
      });
    } else {
      for (const i in this.props.products) {
        if (id === this.props.products[i].id.toString()) {
          this.setState({
            productId: this.props.products[i].id
          });
        }
      }
    }
  };

  eventUpdate = element => {
    const selected = Array.from(element.target.selectedOptions).map((e: any) => parseInt(e.value, 10));
    this.setState({
      idsevent: keysToValues(selected, this.props.events, 'id')
    });
  };

  displayevent(value: any) {
    if (this.state.idsevent && this.state.idsevent.length !== 0) {
      const list = [];
      for (const i in this.state.idsevent) {
        if (this.state.idsevent[i]) {
          list.push(this.state.idsevent[i].id);
        }
      }
      return list;
    }
    if (value.events && value.events.length !== 0) {
      const list = [];
      for (const i in value.events) {
        if (value.events[i]) {
          list.push(value.events[i].id);
        }
      }
      this.setState({
        idsevent: keysToValues(list, this.props.events, 'id')
      });
      return list;
    }
    return null;
  }

  render() {
    const { salesEntity, products, events, loading, updating } = this.props;
    const { isNew } = this.state;

    return (
      <div>
        <Row className="justify-content-center">
          <Col md="8">
            <h2 id="misingpieceApp.sales.home.createOrEditLabel">
              <Translate contentKey="misingpieceApp.sales.home.createOrEditLabel">Create or edit a Sales</Translate>
            </h2>
          </Col>
        </Row>
        <Row className="justify-content-center">
          <Col md="8">
            {loading ? (
              <p>Loading...</p>
            ) : (
              <AvForm model={isNew ? {} : salesEntity} onSubmit={this.saveEntity}>
                {!isNew ? (
                  <AvGroup>
                    <Label for="id">
                      <Translate contentKey="global.field.id">ID</Translate>
                    </Label>
                    <AvInput id="sales-id" type="text" className="form-control" name="id" required readOnly />
                  </AvGroup>
                ) : null}
                <AvGroup>
                  <Label id="typeLabel">
                    <Translate contentKey="misingpieceApp.sales.type">Type</Translate>
                  </Label>
                  <AvInput
                    id="sales-type"
                    type="select"
                    className="form-control"
                    name="type"
                    value={(!isNew && salesEntity.type) || 'ONLINE'}
                  >
                    <option value="ONLINE">
                      <Translate contentKey="misingpieceApp.SalesType.ONLINE" />
                    </option>
                    <option value="EVENT">
                      <Translate contentKey="misingpieceApp.SalesType.EVENT" />
                    </option>
                  </AvInput>
                </AvGroup>
                <AvGroup>
                  <Label id="unitPriceLabel" for="unitPrice">
                    <Translate contentKey="misingpieceApp.sales.unitPrice">Unit Price</Translate>
                  </Label>
                  <AvField
                    id="sales-unitPrice"
                    type="number"
                    className="form-control"
                    name="unitPrice"
                    validate={{
                      required: { value: true, errorMessage: translate('entity.validation.required') },
                      number: { value: true, errorMessage: translate('entity.validation.number') }
                    }}
                  />
                </AvGroup>
                <AvGroup>
                  <Label id="itemCountLabel" for="itemCount">
                    <Translate contentKey="misingpieceApp.sales.itemCount">Item Count</Translate>
                  </Label>
                  <AvField
                    id="sales-itemCount"
                    type="number"
                    className="form-control"
                    name="itemCount"
                    validate={{
                      required: { value: true, errorMessage: translate('entity.validation.required') },
                      number: { value: true, errorMessage: translate('entity.validation.number') }
                    }}
                  />
                </AvGroup>
                <AvGroup>
                  <Label id="totalPriceLabel" for="totalPrice">
                    <Translate contentKey="misingpieceApp.sales.totalPrice">Total Price</Translate>
                  </Label>
                  <AvField
                    id="sales-totalPrice"
                    type="number"
                    className="form-control"
                    name="totalPrice"
                    validate={{
                      required: { value: true, errorMessage: translate('entity.validation.required') },
                      number: { value: true, errorMessage: translate('entity.validation.number') }
                    }}
                  />
                </AvGroup>
                <AvGroup>
                  <Label id="creationDateLabel" for="creationDate">
                    <Translate contentKey="misingpieceApp.sales.creationDate">Creation Date</Translate>
                  </Label>
                  <AvField id="sales-creationDate" type="date" className="form-control" name="creationDate" />
                </AvGroup>
                <AvGroup>
                  <Label id="updateDateLabel" for="updateDate">
                    <Translate contentKey="misingpieceApp.sales.updateDate">Update Date</Translate>
                  </Label>
                  <AvField id="sales-updateDate" type="date" className="form-control" name="updateDate" />
                </AvGroup>
                <AvGroup>
                  <Label id="statusLabel">
                    <Translate contentKey="misingpieceApp.sales.status">Status</Translate>
                  </Label>
                  <AvInput
                    id="sales-status"
                    type="select"
                    className="form-control"
                    name="status"
                    value={(!isNew && salesEntity.status) || 'ACTIVE'}
                  >
                    <option value="ACTIVE">
                      <Translate contentKey="misingpieceApp.Status.ACTIVE" />
                    </option>
                    <option value="HISTORY">
                      <Translate contentKey="misingpieceApp.Status.HISTORY" />
                    </option>
                  </AvInput>
                </AvGroup>
                <AvGroup>
                  <Label for="product.id">
                    <Translate contentKey="misingpieceApp.sales.product">Product</Translate>
                  </Label>
                  <AvInput id="sales-product" type="select" className="form-control" name="product.id" onChange={this.productUpdate}>
                    <option value="" key="0" />
                    {products
                      ? products.map(otherEntity => (
                          <option value={otherEntity.id} key={otherEntity.id}>
                            {otherEntity.id}
                          </option>
                        ))
                      : null}
                  </AvInput>
                </AvGroup>
                <AvGroup>
                  <Label for="events">
                    <Translate contentKey="misingpieceApp.sales.event">Event</Translate>
                  </Label>
                  <AvInput
                    id="sales-event"
                    type="select"
                    multiple
                    className="form-control"
                    name="fakeevents"
                    value={this.displayevent(salesEntity)}
                    onChange={this.eventUpdate}
                  >
                    <option value="" key="0" />
                    {events
                      ? events.map(otherEntity => (
                          <option value={otherEntity.id} key={otherEntity.id}>
                            {otherEntity.id}
                          </option>
                        ))
                      : null}
                  </AvInput>
                  <AvInput id="sales-event" type="hidden" name="events" value={this.state.idsevent} />
                </AvGroup>
                <Button tag={Link} id="cancel-save" to="/entity/sales" replace color="info">
                  <FontAwesomeIcon icon="arrow-left" />&nbsp;
                  <span className="d-none d-md-inline">
                    <Translate contentKey="entity.action.back">Back</Translate>
                  </span>
                </Button>
                &nbsp;
                <Button color="primary" id="save-entity" type="submit" disabled={updating}>
                  <FontAwesomeIcon icon="save" />&nbsp;
                  <Translate contentKey="entity.action.save">Save</Translate>
                </Button>
              </AvForm>
            )}
          </Col>
        </Row>
      </div>
    );
  }
}

const mapStateToProps = (storeState: IRootState) => ({
  products: storeState.product.entities,
  events: storeState.event.entities,
  salesEntity: storeState.sales.entity,
  loading: storeState.sales.loading,
  updating: storeState.sales.updating
});

const mapDispatchToProps = {
  getProducts,
  getEvents,
  getEntity,
  updateEntity,
  createEntity,
  reset
};

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(SalesUpdate);
